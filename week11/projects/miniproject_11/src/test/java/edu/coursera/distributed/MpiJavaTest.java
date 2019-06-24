package edu.coursera.distributed;

import java.util.Random;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.TestSetup;
import junit.framework.TestSuite;

import edu.coursera.distributed.util.MPI;
import edu.coursera.distributed.util.MPI.MPIException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MpiJavaTest extends TestCase {

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

    private Matrix createRandomMatrix(final int rows, final int cols) {
        Matrix matrix = new Matrix(rows, cols);
        final Random rand = new Random(314);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, rand.nextInt(100));
            }
        }

        return matrix;
    }

    private Matrix copyMatrix(Matrix input) {
        return new Matrix(input);
    }

    private void seqMatrixMultiply(Matrix a, Matrix b, Matrix c) {
        for (int i = 0; i < c.getNRows(); i++) {
            for (int j = 0; j < c.getNCols(); j++) {
                c.set(i, j, 0.0);

                for (int k = 0; k < b.getNRows(); k++) {
                    c.incr(i, j, a.get(i, k) * b.get(k, j));
                }
            }
        }
    }

    private static MPI mpi = null;

    public static Test suite() {
        TestSetup setup = new TestSetup(new TestSuite(MpiJavaTest.class)) {
            protected void setUp() throws Exception {
                assert (mpi == null);
                mpi = new MPI();
                mpi.MPI_Init();
            }

            protected void tearDown() throws Exception {
                assert (mpi != null);
                mpi.MPI_Finalize();
            }
        };
        return setup;
    }

    private void testDriver(final int M, final int N, final int P)
            throws MPIException {
        final int myrank = mpi.MPI_Comm_rank(mpi.MPI_COMM_WORLD);

        Matrix a, b, c;
        if (myrank == 0) {
            a = createRandomMatrix(M, N);
            b = createRandomMatrix(N, P);
            c = createRandomMatrix(M, P);
        } else {
            a = new Matrix(M, N);
            b = new Matrix(N, P);
            c = new Matrix(M, P);
        }

        Matrix copy_a = copyMatrix(a);
        Matrix copy_b = copyMatrix(b);
        Matrix copy_c = copyMatrix(c);

        if (myrank == 0) {
            System.err.println("Testing matrix multiply: [" + M + " x " + N +
                    "] * [" + N + " x " + P + "] = [" + M + " x " + P + "]");
        }

        final long seqStart = System.currentTimeMillis();
        seqMatrixMultiply(copy_a, copy_b, copy_c);
        final long seqElapsed = System.currentTimeMillis() - seqStart;

        if (myrank == 0) {
            System.err.println("Sequential implementation ran in " +
                    seqElapsed + " ms");
        }

        mpi.MPI_Barrier(mpi.MPI_COMM_WORLD);

        final long parallelStart = System.currentTimeMillis();
        MatrixMult.parallelMatrixMultiply(a, b, c, mpi);
        final long parallelElapsed = System.currentTimeMillis() - parallelStart;


        if (myrank == 0) {
            final double speedup = (double)seqElapsed / (double)parallelElapsed;
            System.err.println("MPI implementation ran in " + parallelElapsed +
                    " ms, yielding a speedup of " + speedup + "x");
            System.err.println();

            for (int i = 0; i < c.getNRows(); i++) {
                for (int j = 0; j < c.getNCols(); j++) {
                    final String msg = "Expected " + copy_c.get(i, j)
                        + " at (" + i + ", " + j + ") but found " + c.get(i, j);
                    assertTrue(msg, copy_c.get(i, j) == c.get(i, j));
                }
            }

            final double expectedSpeedup = 0.75 * getNCores();
            String msg = "Expected a speedup of at least " + expectedSpeedup
                + ", but saw " + speedup;
            assertTrue(msg, speedup >= expectedSpeedup);
        }
    }

    public void testMatrixMultiplySquareSmall() throws MPIException {
        testDriver(800, 800, 800);
    }

    public void testMatrixMultiplySquareLarge() throws MPIException {
        testDriver(1200, 1200, 1200);
    }

    public void testMatrixMultiplyRectangular1Small() throws MPIException {
        testDriver(800, 1600, 500);
    }

    public void testMatrixMultiplyRectangular2Small() throws MPIException {
        testDriver(1600, 800, 500);
    }

    public void testMatrixMultiplyRectangularLarge() throws MPIException {
        testDriver(1800, 1400, 1000);
    }
}
