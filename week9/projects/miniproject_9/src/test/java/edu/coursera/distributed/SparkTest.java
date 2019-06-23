package edu.coursera.distributed;

import scala.Tuple2;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import junit.framework.TestCase;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkTest extends TestCase {

    private static enum EdgeDistribution {
        INCREASING,
        RANDOM,
        UNIFORM
    }

    private static JavaSparkContext getSparkContext(final int nCores) {
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        final SparkConf conf = new SparkConf()
            .setAppName("edu.coursera.distributed.PageRank")
            .setMaster("local[" + nCores + "]")
            .set("spark.ui.showConsoleProgress", "false");
        JavaSparkContext ctx = new JavaSparkContext(conf);
        ctx.setLogLevel("OFF");
        return ctx;
    }

    private static int getNCores() {
        String ncoresStr = System.getenv("COURSERA_GRADER_NCORES");
        if (ncoresStr == null) {
            ncoresStr = System.getProperty("COURSERA_GRADER_NCORES");
        }

        if (ncoresStr == null) {
            return Runtime.getRuntime().availableProcessors();
        } else {
            return Integer.parseInt(ncoresStr);
        }
    }

    private static Website generateWebsite(final int i, final int nNodes,
            final int minEdgesPerNode, final int maxEdgesPerNode,
            final EdgeDistribution edgeConfig) {
        Random r = new Random(i);

        Website site = new Website(i);

        final int nEdges;
        switch (edgeConfig) {
            case INCREASING:
                double frac = (double)i / (double)nNodes;
                double offset = (double)(maxEdgesPerNode - minEdgesPerNode)
                    * frac;
                nEdges = minEdgesPerNode + (int)offset;
                break;
            case RANDOM:
                nEdges = minEdgesPerNode +
                    r.nextInt(maxEdgesPerNode - minEdgesPerNode);
                break;
            case UNIFORM:
                nEdges = maxEdgesPerNode;
                break;
            default:
                throw new RuntimeException();
        }

        for (int j = 0; j < nEdges; j++) {
            site.addEdge(r.nextInt(nNodes));
        }

        return site;
    }

    private static JavaPairRDD<Integer, Website> generateGraphRDD(
            final int nNodes, final int minEdgesPerNode,
            final int maxEdgesPerNode, final EdgeDistribution edgeConfig,
            final JavaSparkContext context) {
        List<Integer> nodes = new ArrayList<Integer>(nNodes);
        for (int i = 0; i < nNodes; i++) {
            nodes.add(i);
        }

        return context.parallelize(nodes).mapToPair(i -> {
            return new Tuple2(i, generateWebsite(i, nNodes, minEdgesPerNode,
                    maxEdgesPerNode, edgeConfig));
        });
    }

    private static JavaPairRDD<Integer, Double> generateRankRDD(
            final int nNodes, final JavaSparkContext context) {
        List<Integer> nodes = new ArrayList<Integer>(nNodes);
        for (int i = 0; i < nNodes; i++) {
            nodes.add(i);
        }

        return context.parallelize(nodes).mapToPair(i -> {
            Random rand = new Random(i);
            return new Tuple2(i, 100.0 * rand.nextDouble());
        });
    }

    private static Website[] generateGraphArr(final int nNodes,
            final int minEdgesPerNode, final int maxEdgesPerNode,
            final EdgeDistribution edgeConfig) {
        Website[] sites = new Website[nNodes];
        for (int i = 0; i < sites.length; i++) {
            sites[i] = generateWebsite(i, nNodes, minEdgesPerNode,
                    maxEdgesPerNode, edgeConfig);
        }
        return sites;
    }

    private static double[] generateRankArr(final int nNodes) {
        double[] ranks = new double[nNodes];
        for (int i = 0; i < ranks.length; i++) {
            Random r = new Random(i);
            ranks[i] = 100.0 * r.nextDouble();
        }
        return ranks;
    }

    private static double[] seqPageRank(Website[] sites, double[] ranks) {
        double[] newRanks = new double[ranks.length];

        for (int j = 0; j < sites.length; j++) {
            Iterator<Integer> iter = sites[j].edgeIterator();
            while (iter.hasNext()) {
                int target = iter.next();
                newRanks[target] += ranks[j] / (double)sites[j].getNEdges();
            }
        }

        for (int j = 0; j < newRanks.length; j++) {
            newRanks[j] = 0.15 + 0.85 * newRanks[j];
        }

        return newRanks;
    }

    private static void testDriver(final int nNodes, final int minEdgesPerNode,
            final int maxEdgesPerNode, final int niterations,
            final EdgeDistribution edgeConfig) {
        System.err.println("Running the PageRank algorithm for " + niterations +
                " iterations on a website graph of " + nNodes + " websites");
        System.err.println();

        final int repeats = 2;
        Website[] nodesArr = generateGraphArr(nNodes, minEdgesPerNode,
                maxEdgesPerNode, edgeConfig);
        double[] ranksArr = generateRankArr(nNodes);
        for (int i = 0; i < niterations; i++) {
            ranksArr = seqPageRank(nodesArr, ranksArr);
        }

        JavaSparkContext context = getSparkContext(1);

        JavaPairRDD<Integer, Website> nodes = null;
        JavaPairRDD<Integer, Double> ranks = null;
        final long singleStart = System.currentTimeMillis();
        for (int r = 0; r < repeats; r++) {
            nodes = generateGraphRDD(nNodes, minEdgesPerNode,
                    maxEdgesPerNode, edgeConfig, context);
            ranks = generateRankRDD(nNodes, context);
            for (int i = 0; i < niterations; i++) {
                ranks = PageRank.sparkPageRank(nodes, ranks);
            }
            List<Tuple2<Integer, Double>> parResult = ranks.collect();
        }
        final long singleElapsed = System.currentTimeMillis() - singleStart;
        context.stop();

        context = getSparkContext(getNCores());

        List<Tuple2<Integer, Double>> parResult = null;
        final long parStart = System.currentTimeMillis();
        for (int r = 0; r < repeats; r++) {
            nodes = generateGraphRDD(nNodes, minEdgesPerNode,
                    maxEdgesPerNode, edgeConfig, context);
            ranks = generateRankRDD(nNodes, context);
            for (int i = 0; i < niterations; i++) {
                ranks = PageRank.sparkPageRank(nodes, ranks);
            }
            parResult = ranks.collect();
        }
        final long parElapsed = System.currentTimeMillis() - parStart;
        final double speedup = (double)singleElapsed / (double)parElapsed;
        context.stop();

        Map<Integer, Double> keyed = new HashMap<Integer, Double>();
        for (Tuple2<Integer, Double> site : parResult) {
            assert (!keyed.containsKey(site._1()));
            keyed.put(site._1(), site._2());
        }

        assertEquals(nodesArr.length, parResult.size());
        for (int i = 0; i < parResult.size(); i++) {
            assertTrue(keyed.containsKey(nodesArr[i].getId()));
            final double delta = Math.abs(ranksArr[i] -
                    keyed.get(nodesArr[i].getId()));
            assertTrue(delta < 1E-9);
        }

        System.err.println();
        System.err.println("Single-core execution ran in " + singleElapsed +
                " ms");
        System.err.println(getNCores() + "-core execution ran in " +
                parElapsed + " ms, yielding a speedup of " + speedup + "x");
        System.err.println();

        final double expectedSpeedup = 1.35;
        final String msg = "Expected at least " + expectedSpeedup +
            "x speedup, but only saw " + speedup + "x. Sequential time = " +
            singleElapsed + " ms, parallel time = " + parElapsed + " ms";
        assertTrue(msg, speedup >= expectedSpeedup);
    }

    public void testUniformTwentyThousand() {
        final int nNodes = 20000;
        final int minEdgesPerNode = 20;
        final int maxEdgesPerNode = 40;
        final int niterations = 5;
        final EdgeDistribution edgeConfig = EdgeDistribution.UNIFORM;

        testDriver(nNodes, minEdgesPerNode, maxEdgesPerNode, niterations,
                edgeConfig);
    }

    public void testUniformFiftyThousand() {
        final int nNodes = 50000;
        final int minEdgesPerNode = 20;
        final int maxEdgesPerNode = 40;
        final int niterations = 5;
        final EdgeDistribution edgeConfig = EdgeDistribution.UNIFORM;

        testDriver(nNodes, minEdgesPerNode, maxEdgesPerNode, niterations,
                edgeConfig);
    }

    public void testIncreasingTwentyThousand() {
        final int nNodes = 20000;
        final int minEdgesPerNode = 20;
        final int maxEdgesPerNode = 40;
        final int niterations = 5;
        final EdgeDistribution edgeConfig = EdgeDistribution.INCREASING;

        testDriver(nNodes, minEdgesPerNode, maxEdgesPerNode, niterations,
                edgeConfig);
    }

    public void testIncreasingFiftyThousand() {
        final int nNodes = 50000;
        final int minEdgesPerNode = 20;
        final int maxEdgesPerNode = 40;
        final int niterations = 5;
        final EdgeDistribution edgeConfig = EdgeDistribution.INCREASING;

        testDriver(nNodes, minEdgesPerNode, maxEdgesPerNode, niterations,
                edgeConfig);
    }

    public void testRandomTwentyThousand() {
        final int nNodes = 20000;
        final int minEdgesPerNode = 20;
        final int maxEdgesPerNode = 40;
        final int niterations = 5;
        final EdgeDistribution edgeConfig = EdgeDistribution.RANDOM;

        testDriver(nNodes, minEdgesPerNode, maxEdgesPerNode, niterations,
                edgeConfig);
    }

    public void testRandomFiftyThousand() {
        final int nNodes = 50000;
        final int minEdgesPerNode = 20;
        final int maxEdgesPerNode = 40;
        final int niterations = 5;
        final EdgeDistribution edgeConfig = EdgeDistribution.RANDOM;

        testDriver(nNodes, minEdgesPerNode, maxEdgesPerNode, niterations,
                edgeConfig);
    }
}
