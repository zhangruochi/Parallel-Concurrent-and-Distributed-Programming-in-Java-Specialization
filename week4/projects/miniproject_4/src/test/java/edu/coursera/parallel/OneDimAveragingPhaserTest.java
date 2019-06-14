package edu.coursera.parallel;

import java.util.Random;
import java.util.concurrent.Phaser;

import junit.framework.TestCase;

public class OneDimAveragingPhaserTest extends TestCase {
    // Number of times to repeat each test, for consistent timing results.
    final static private int niterations = 12000;

    private static int getNCores() {
        String ncoresStr = System.getenv("COURSERA_GRADER_NCORES");
        if (ncoresStr == null) {
            return Runtime.getRuntime().availableProcessors();
        } else {
            return Integer.parseInt(ncoresStr);
        }
    }

    private double[] createArray(final int N, final int iterations) {
        final double[] input = new double[N + 2];
        int index = N + 1;
        while (index > 0) {
            input[index] = 1.0;
            index -= (iterations / 4);
        }
        return input;
    }

    /**
     * A reference implementation of runSequential, in case the one in the main source file is accidentally modified.
     */
    public void runSequential(final int iterations, double[] myNew, double[] myVal, final int n) {
        for (int iter = 0; iter < iterations; iter++) {
            for (int j = 1; j <= n; j++) {
                myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
            }
            double[] tmp = myNew;
            myNew = myVal;
            myVal = tmp;
        }
    }

    private static void runParallelBarrier(final int iterations, final double[] myNew, final double[] myVal,
            final int n, final int tasks) {
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);

        Thread[] threads = new Thread[tasks];

        for (int ii = 0; ii < tasks; ii++) {
            final int i = ii;

            threads[ii] = new Thread(() -> {
                double[] threadPrivateMyVal = myVal;
                double[] threadPrivateMyNew = myNew;

                final int chunkSize = (n + tasks - 1) / tasks;
                final int left = (i * chunkSize) + 1;
                int right = (left + chunkSize) - 1;
                if (right > n) right = n;

                for (int iter = 0; iter < iterations; iter++) {

                    for (int j = left; j <= right; j++) {
                        threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1] + threadPrivateMyVal[j + 1]) / 2.0;
                    }
                    ph.arriveAndAwaitAdvance();

                    double[] temp = threadPrivateMyNew;
                    threadPrivateMyNew = threadPrivateMyVal;
                    threadPrivateMyVal = temp;
                }
            });
            threads[ii].start();
        }

        for (int ii = 0; ii < tasks; ii++) {
            try {
                threads[ii].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void checkResult(final double[] ref, final double[] output) {
        for (int i = 0; i < ref.length; i++) {
            String msg = "Mismatch on output at element " + i;
            assertEquals(msg, ref[i], output[i]);
        }
    }

    /**
     * A helper function for tests of the two-task parallel implementation.
     *
     * @param N The size of the array to test
     * @return The speedup achieved, not all tests use this information
     */
    private double parTestHelper(final int N, final int ntasks) {
        // Create a random input
        double[] myNew = createArray(N, niterations);
        double[] myVal = createArray(N, niterations);
        final double[] myNewRef = createArray(N, niterations);
        final double[] myValRef = createArray(N, niterations);

        final long barrierStartTime = System.currentTimeMillis();
        runParallelBarrier(niterations, myNew, myVal, N, ntasks);
        final long barrierEndTime = System.currentTimeMillis();

        final long fuzzyStartTime = System.currentTimeMillis();
        OneDimAveragingPhaser.runParallelFuzzyBarrier(niterations, myNewRef, myValRef, N, ntasks);
        final long fuzzyEndTime = System.currentTimeMillis();

        if (niterations % 2 == 0) {
            checkResult(myNew, myNewRef);
        } else {
            checkResult(myVal, myValRef);
        }

        return (double)(barrierEndTime - barrierStartTime) / (double)(fuzzyEndTime - fuzzyStartTime);
    }

    /**
     * Test on large input.
     */
    public void testFuzzyBarrier() {
        final double expected = 1.05;
        final double speedup = parTestHelper(4 * 1024 * 1024, getNCores() * 16);
        final String errMsg = String.format("It was expected that the fuzzy barrier parallel implementation would " +
                "run %fx faster than the barrier implementation, but it only achieved %fx speedup", expected, speedup);
        assertTrue(errMsg, speedup >= expected);
    }
}
