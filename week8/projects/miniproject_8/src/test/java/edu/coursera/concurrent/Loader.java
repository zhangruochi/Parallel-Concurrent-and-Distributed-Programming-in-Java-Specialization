package edu.coursera.concurrent;

import edu.coursera.concurrent.util.IntPair;
import edu.coursera.concurrent.boruvka.Edge;
import edu.coursera.concurrent.boruvka.Component;
import edu.coursera.concurrent.boruvka.BoruvkaFactory;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.Queue;

/**
 * This class should not be modified.
 *
 * @author <a href="http://shams.web.rice.edu/">Shams Imam</a> (shams@rice.edu)
 */
public final class Loader {
    /**
     * Read edges from the provided input file.
     */
    public static <C extends Component, E extends Edge> void read(final String fileName,
            final BoruvkaFactory<C, E> boruvkaFactory, final Queue<C> nodesLoaded) {

        final Map<Integer, C> nodesMap = new HashMap<>();
        final Map<IntPair, E> edgesMap = new HashMap<>();

        double totalWeight = 0;
        int edges = 0;
        try {
            // Open the compressed file
            final GZIPInputStream in = new GZIPInputStream(new FileInputStream(fileName));
            final Reader r = new BufferedReader(new InputStreamReader(in));
            final StreamTokenizer st = new StreamTokenizer(r);
            final String cstring = "c";
            final String pstring = "p";
            st.commentChar(cstring.charAt(0));
            st.commentChar(pstring.charAt(0));
            // read graph
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                assert (st.sval.equals("a"));
                st.nextToken();
                final int from = (int) st.nval;
                st.nextToken();
                final int to = (int) st.nval;
                final C nodeFrom = getComponent(boruvkaFactory, nodesMap, from);
                final C nodeTo = getComponent(boruvkaFactory, nodesMap, to);
                assert (nodeTo != nodeFrom); // Assume no self-loops in the input graph
                st.nextToken();
                final int weight = (int) st.nval;
                addEdge(boruvkaFactory, edgesMap, from, to, nodeFrom, nodeTo, weight);
                totalWeight += weight;
                edges++;
            }
            // Close the file and stream
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final List<C> nodesList = new ArrayList<>();
        nodesList.addAll(nodesMap.values());
        Collections.shuffle(nodesList);
        nodesLoaded.addAll(nodesList);
    }

    private static <C extends Component, E extends Edge> C getComponent(final BoruvkaFactory<C, E> factory,
            final Map<Integer, C> nodesMap, final int node) {
        if (!nodesMap.containsKey(node)) {
            nodesMap.put(node, factory.newComponent(node));
        }
        return nodesMap.get(node);
    }

    private static <C extends Component, E extends Edge> void addEdge(
            final BoruvkaFactory<C, E> factory, final Map<IntPair, E> edgesMap,
            final int from, final int to, final C fromC, final C toC, final double w) {

        final IntPair p;
        if (from < to) {
            p = new IntPair(from, to);
        } else {
            p = new IntPair(to, from);
        }
        if (!edgesMap.containsKey(p)) {
            final E e = factory.newEdge(fromC, toC, w);
            edgesMap.put(p, e);
            fromC.addEdge(e);
            toC.addEdge(e);
        } else {
            assert (edgesMap.get(p).weight() == w);
        }
    }
}
