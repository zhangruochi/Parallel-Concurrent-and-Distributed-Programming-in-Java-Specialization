package edu.coursera.distributed;

import edu.coursera.distributed.util.MPI;
import edu.coursera.distributed.util.MPI.MPIException;

/**
 * A wrapper class for a parallel, MPI-based matrix multiply implementation.
 */
public class MatrixMult {
    /**
     * A parallel implementation of matrix multiply using MPI to express SPMD
     * parallelism. In particular, this method should store the output of
     * multiplying the matrices a and b into the matrix c.
     *
     * This method is called simultaneously by all MPI ranks in a running MPI
     * program. For simplicity MPI_Init has already been called, and
     * MPI_Finalize should not be called in parallelMatrixMultiply.
     *
     * On entry to parallelMatrixMultiply, the following will be true of a, b,
     * and c:
     *
     *   1) The matrix a will only be filled with the input values on MPI rank
     *      zero. Matrix a on all other ranks will be empty (initialized to all
     *      zeros).
     *   2) Likewise, the matrix b will only be filled with input values on MPI
     *      rank zero. Matrix b on all other ranks will be empty (initialized to
     *      all zeros).
     *   3) Matrix c will be initialized to all zeros on all ranks.
     *
     * Upon returning from parallelMatrixMultiply, the following must be true:
     *
     *   1) On rank zero, matrix c must be filled with the final output of the
     *      full matrix multiplication. The contents of matrix c on all other
     *      ranks are ignored.
     *
     * Therefore, it is the responsibility of this method to distribute the
     * input data in a and b across all MPI ranks for maximal parallelism,
     * perform the matrix multiply in parallel, and finally collect the output
     * data in c from all ranks back to the zeroth rank. You may use any of the
     * MPI APIs provided in the mpi object to accomplish this.
     *
     * A reference sequential implementation is provided below, demonstrating
     * the use of the Matrix class's APIs.
     *
     * @param a Input matrix
     * @param b Input matrix
     * @param c Output matrix
     * @param mpi MPI object supporting MPI APIs
     * @throws MPIException On MPI error. It is not expected that your
     *                      implementation should throw any MPI errors during
     *                      normal operation.
     */
    public static void parallelMatrixMultiply(Matrix a, Matrix b, Matrix c,
                                              final MPI mpi) throws MPIException {


        /**
         *  SPMD -> Same program running on all processes / ranks
         *  MPI_COMM_WORLD is the communicator
         *  Matrix is implemented as 1D array as MPI uses 1D buffer to send / receive
         */

        // Each process has a Rank to Identify it

        final int myRank = mpi.MPI_Comm_rank(mpi.MPI_COMM_WORLD);

        // Number of Processes
        final int size = mpi.MPI_Comm_size(mpi.MPI_COMM_WORLD);

        final int rows = c.getNRows();
        final int rowChunk = (rows + size - 1) / size;
        // Divide into Chunks for each process. Each Process runs only the row chunk

        // Get Start and End Index of each chunk
        final int start = myRank * rowChunk;
        int end = (myRank + 1) * rowChunk;
        if (end > rows) end = rows;
        //  Edge Case check -> endIndex is bound by actual Size of Resultant matrix

        //BroadCast Matrix a and b to other ranks
        //Broadcast sends data in array => need to convert 2D Matrix to Array
        mpi.MPI_Bcast(a.getValues(), 0, a.getNRows() * a.getNCols(), 0, mpi.MPI_COMM_WORLD);
        //BroadCast sends array of info, takes offset, num of elements, Root Rank, Communicator as args
        mpi.MPI_Bcast(b.getValues(), 0, b.getNRows() * b.getNCols(), 0 , mpi.MPI_COMM_WORLD);



        for (int i = start; i < end; i++) {
            for (int j = 0; j < c.getNCols(); j++) {
                c.set(i, j, 0.0);
                for (int k = 0; k < b.getNRows(); k++) {
                    c.incr(i, j, a.get(i, k) * b.get(k, j));
                }
            }
        }

        // Gather Result if Rank 0, Send Results if Rank != 0
        if (myRank == 0) {

            // Buffer for the Other Process's Results that Rank 0 will receive
            MPI.MPI_Request [] requests = new MPI.MPI_Request[size - 1];

            // Iterate through Ranks and Receive their Results in non Blocking way
            for (int i = 1; i < size; i++) {
                // get Row Start and Row End for ith Chunk
                final int rankStartRow = i * rowChunk;
                int rankEndRow = (i+1) * rowChunk;

                // EdgeCase if Rank End row -> (i + 1) * chunkSize is greater than number of Actual Rows
                if (rankEndRow > rows) rankEndRow = rows;

                final int rowOffset = rankStartRow * c.getNCols();

                //Number of Elements in the chunk
                final int nElements = ( rankEndRow - rankStartRow ) * c.getNCols();

                // Non Blocking Receive from other ranks
                requests[i-1] = mpi.MPI_Irecv(c.getValues(), rowOffset, nElements, i, i, mpi.MPI_COMM_WORLD);
                // Buffer to Receive, RowOffset, num of Elements, Rank of Sender, Tag for Message, Comm
            }
            mpi.MPI_Waitall(requests);
            // like async-await, wait for all other ranks to return result (Async -> reason for Speedup)

        } else {
            // Other Ranks send their Results to rook rank
            mpi.MPI_Send(c.getValues(), start * c.getNCols(), (end - start) * c.getNCols(), 0, myRank, mpi.MPI_COMM_WORLD);
            // Buffer to send , offset, num elements, destination rank, myrank, comm
        }
    }
}