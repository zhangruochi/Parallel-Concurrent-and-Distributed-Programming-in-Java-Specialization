package edu.coursera.distributed.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;

import java.nio.IntBuffer;
import java.nio.DoubleBuffer;

/**
 * Wrapper class for all supported Java MPI APIs. For more detailed
 * documentation of the wrapped MPI APIs, refer to
 * https://www.open-mpi.org/doc/v1.8/
 */
public final class MPI {
    private interface MPILib extends Library {
        MPILib INSTANCE = (MPILib)Native.loadLibrary("mpi", MPILib.class);
        NativeLibrary lib = NativeLibrary.getInstance("mpi");

        int MPI_Send(IntBuffer buf, int count, Pointer datatype,
                int dest, int tag, Pointer comm);
        int MPI_Send(DoubleBuffer buf, int count, Pointer datatype,
                int dest, int tag, Pointer comm);

        int MPI_Recv(IntBuffer buf, int count, Pointer datatype, int src,
                int tag, Pointer comm, Pointer status);
        int MPI_Recv(DoubleBuffer buf, int count, Pointer datatype,
                int src, int tag, Pointer comm, Pointer status);

        int MPI_Isend(Memory buf, int count, Pointer datatype,
                int dest, int tag, Pointer comm, Memory request);

        int MPI_Irecv(Memory buf, int count, Pointer datatype,
                int src, int tag, Pointer comm, Memory request);

        int MPI_Bcast(DoubleBuffer buf, int count, Pointer datatype,
                int root, Pointer comm);

        int MPI_Wait(Memory request, Pointer status);

        int MPI_Barrier(Pointer comm);

        int MPI_Init(int[] argc, String[] argv);
        int MPI_Finalize();
        int MPI_Comm_size(Pointer comm, int[] size);
        int MPI_Comm_rank(Pointer comm, int[] rank);
    }

    /**
     * Constant wrapper for communicator MPI_COMM_WORLD.
     */
    public final MPI_Comm MPI_COMM_WORLD;
    /**
     * Constant wrapper for datatype MPI_INTEGER.
     */
    public final MPI_Datatype MPI_INTEGER;
    /**
     * Constant wrapper for datatype MPI_FLOAT.
     */
    public final MPI_Datatype MPI_FLOAT;
    /**
     * Constant wrapper for datatype MPI_DOUBLE.
     */
    public final MPI_Datatype MPI_DOUBLE;
    /**
     * Constant wrapper for MPI_STATUS_IGNORE.
     */
    public final MPI_Status MPI_STATUS_IGNORE;

    /**
     * Constructor.
     */
    public MPI() {
        Pointer ptr = MPILib.lib.getGlobalVariableAddress(
                "ompi_mpi_comm_world");
        MPI_COMM_WORLD = new MPI_Comm(ptr);

        ptr = MPILib.lib.getGlobalVariableAddress("ompi_mpi_integer");
        MPI_INTEGER = new MPI_Datatype(ptr);

        ptr = MPILib.lib.getGlobalVariableAddress("ompi_mpi_float");
        MPI_FLOAT = new MPI_Datatype(ptr);

        ptr = MPILib.lib.getGlobalVariableAddress("ompi_mpi_double");
        MPI_DOUBLE = new MPI_Datatype(ptr);

        MPI_STATUS_IGNORE = new MPI_Status(Pointer.NULL);
    }

    /**
     * Java wrapper for MPI_Init.
     *
     * @throws MPIException On MPI error
     */
    public void MPI_Init() throws MPIException {
        final int err = MPILib.INSTANCE.MPI_Init(new int[0], new String[0]);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Finalize.
     *
     * @throws MPIException On MPI error
     */
    public void MPI_Finalize() throws MPIException {
        final int err = MPILib.INSTANCE.MPI_Finalize();
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Comm_size.
     *
     * @param comm Communicator
     * @return Number of MPI ranks in the provided communicator
     * @throws MPIException On MPI error
     */
    public int MPI_Comm_size(final MPI_Comm comm) throws MPIException {
        int[] result = new int[1];
        final int err = MPILib.INSTANCE.MPI_Comm_size(comm.comm, result);
        if (err != 0) {
            throw new MPIException(err);
        }
        return result[0];
    }

    /**
     * Java wrapper for MPI_Comm_rank.
     *
     * @param comm Communicator
     * @return Integer rank of the current process in the specified communicator
     * @throws MPIException On MPI error
     */
    public int MPI_Comm_rank(final MPI_Comm comm) throws MPIException {
        int[] result = new int[1];
        final int err = MPILib.INSTANCE.MPI_Comm_rank(comm.comm, result);
        if (err != 0) {
            throw new MPIException(err);
        }
        return result[0];
    }

    /**
     * Java wrapper for MPI_Bcast.
     *
     * @param buf JVM buffer to send from/receive to in bcast
     * @param offset Offset in buf to perform bcast from, in elements
     * @param count Number of elements in buf to perform bcast with, starting at
     *              offset
     * @param root Root rank of this broadcast
     * @param comm Communicator to use
     * @throws MPIException On MPI error
     */
    public void MPI_Bcast(final double[] buf, final int offset, final int count,
            final int root, final MPI_Comm comm) throws MPIException {
        final DoubleBuffer wrapper = DoubleBuffer.wrap(buf, offset, count);
        final int err = MPILib.INSTANCE.MPI_Bcast(wrapper, count,
                MPI_DOUBLE.datatype, root, comm.comm);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Send.
     *
     * @param buf JVM buffer to send from
     * @param offset Offset in buf to send from, in elements
     * @param count Number of elements in buf to send, starting at offset
     * @param dst MPI rank to send to
     * @param tag Tag for this send
     * @param comm Communicator to use
     * @throws MPIException On MPI error
     */
    public void MPI_Send(final int[] buf, final int offset, final int count,
            final int dst, final int tag, final MPI_Comm comm)
            throws MPIException {
        final IntBuffer wrapper = IntBuffer.wrap(buf, offset, count);
        final int err = MPILib.INSTANCE.MPI_Send(wrapper, count,
                MPI_INTEGER.datatype, dst, tag, comm.comm);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Send.
     *
     * @param buf JVM buffer to send from
     * @param offset Offset in buf to send from, in elements
     * @param count Number of elements in buf to send, starting at offset
     * @param dst MPI rank to send to
     * @param tag Tag for this send
     * @param comm Communicator to use
     * @throws MPIException On MPI error
     */
    public void MPI_Send(final double[] buf, final int offset, final int count,
            final int dst, final int tag, final MPI_Comm comm)
            throws MPIException {
        final DoubleBuffer wrapper = DoubleBuffer.wrap(buf, offset, count);
        final int err = MPILib.INSTANCE.MPI_Send(wrapper, count,
                MPI_DOUBLE.datatype, dst, tag, comm.comm);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Recv.
     *
     * @param buf JVM buffer to receive into
     * @param offset Offset in buf to receive to, in elements
     * @param count Number of elements to receive into buf, starting at offset
     * @param src MPI rank to receive from
     * @param tag Tag for this receive
     * @param comm Communicator to use
     * @throws MPIException On MPI error
     */
    public void MPI_Recv(final int[] buf, final int offset, final int count,
            final int src, final int tag, final MPI_Comm comm)
            throws MPIException {
        final IntBuffer wrapper = IntBuffer.wrap(buf, offset, count);
        final int err = MPILib.INSTANCE.MPI_Recv(wrapper, count,
                MPI_INTEGER.datatype, src, tag, comm.comm,
                MPI_STATUS_IGNORE.status);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Recv.
     *
     * @param buf JVM buffer to receive into
     * @param offset Offset in buf to receive to, in elements
     * @param count Number of elements to receive into buf, starting at offset
     * @param src MPI rank to receive from
     * @param tag Tag for this receive
     * @param comm Communicator to use
     * @throws MPIException On MPI error
     */
    public void MPI_Recv(final double[] buf, final int offset, final int count,
            final int src, final int tag, final MPI_Comm comm)
            throws MPIException {
        final DoubleBuffer wrapper = DoubleBuffer.wrap(buf, offset, count);
        final int err = MPILib.INSTANCE.MPI_Recv(wrapper, count,
                MPI_DOUBLE.datatype, src, tag, comm.comm,
                MPI_STATUS_IGNORE.status);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Java wrapper for MPI_Irecv.
     *
     * @param buf JVM buffer to receive into
     * @param offset Offset in buf to receive to, in elements
     * @param count Number of elements to receive into buf, starting at offset
     * @param src MPI rank to receive from
     * @param tag Tag for this receive
     * @param comm Communicator to use
     * @return A request handle that can be used to wait on this async operation
     * @throws MPIException On MPI error
     */
    public MPI_Request MPI_Irecv(final double[] buf, final int offset,
            final int count, final int src, final int tag, final MPI_Comm comm)
            throws MPIException {
        Recv_MPI_Request request = new Recv_MPI_Request(count * 8, buf, offset,
                count);
        final int err = MPILib.INSTANCE.MPI_Irecv(request.buf, count,
                MPI_DOUBLE.datatype, src, tag, comm.comm,
                request.request);
        if (err != 0) {
            throw new MPIException(err);
        }
        return request;
    }

    /**
     * Java wrapper for MPI_Isend.
     *
     * @param buf JVM buffer to send from
     * @param offset Offset in buf to send from, in elements
     * @param count Number of elements in buf to send, starting at offset
     * @param dst MPI rank to send to
     * @param tag Tag for this send
     * @param comm Communicator to use
     * @return A request handle that can be used to wait on this async operation
     * @throws MPIException On MPI error
     */
    public MPI_Request MPI_Isend(final double[] buf, final int offset,
            final int count, final int dst, final int tag, final MPI_Comm comm)
            throws MPIException {
        Send_MPI_Request request = new Send_MPI_Request(count * 8);
        request.buf.write(0, buf, offset, count);
        final int err = MPILib.INSTANCE.MPI_Isend(request.buf, count,
                MPI_DOUBLE.datatype, dst, tag, comm.comm,
                request.request);
        if (err != 0) {
            throw new MPIException(err);
        }
        return request;
    }

    /**
     * Java wrapper for MPI_Wait.
     *
     * @param request Request handle to wait on
     * @throws MPIException On MPI error
     */
    public void MPI_Wait(final MPI_Request request) throws MPIException {
        final int err = MPILib.INSTANCE.MPI_Wait(request.request,
                MPI_STATUS_IGNORE.status);
        if (err != 0) {
            throw new MPIException(err);
        }
        request.complete();
    }

    /**
     * Java wrapper for MPI_Waitall.
     *
     * @param requests Request handles to wait on
     * @throws MPIException On MPI error
     */
    public void MPI_Waitall(final MPI_Request[] requests) throws MPIException {
        for (int i = 0; i < requests.length; i++) {
            MPI_Wait(requests[i]);
        }
    }

    /**
     * Java wrapper for MPI_Barrier.
     *
     * @param comm Communicator to use in the MPI_Barrier
     * @throws MPIException On MPI error
     */
    public void MPI_Barrier(final MPI_Comm comm) throws MPIException {
        final int err = MPILib.INSTANCE.MPI_Barrier(comm.comm);
        if (err != 0) {
            throw new MPIException(err);
        }
    }

    /**
     * Wrapper for an MPI_Comm.
     */
    public static class MPI_Comm {
        /**
         * Pointer to the native communicator.
         */
        protected final Pointer comm;

        /**
         * Constructor.
         *
         * @param setComm Initialize the communicator to use
         */
        public MPI_Comm(final Pointer setComm) {
            this.comm = setComm;
        }
    }

    /**
     * Wrapper for an MPI_Datatype.
     */
    public static final class MPI_Datatype {
        /**
         * Pointer to native memory storing the actual datatype info.
         */
        protected final Pointer datatype;

        /**
         * Constructor.
         *
         * @param setDatatype Initialize the wrapped datatype
         */
        public MPI_Datatype(final Pointer setDatatype) {
            this.datatype = setDatatype;
        }
    }

    /**
     * Wrapper class for an MPI_Request representing a handle to an asynchronous
     * MPI communication.
     */
    public abstract static class MPI_Request {
        /**
         * Wrapped request object.
         */
        protected final Memory request;
        /**
         * Pointer to the buffer that the asynchronous operation is copying to
         * or from. This must be a native buffer to prevent JVM GC from moving
         * it around during the asynchronous communication.
         */
        protected final Memory buf;

        /**
         * Constructor.
         *
         * @param size Size of the copy being performed
         */
        public MPI_Request(final long size) {
            request = new Memory(8);
            buf = new Memory(size);
        }

        /**
         * Method to be called when the asynchronous communication completes.
         */
        protected abstract void complete();
    }

    /**
     * An MPI_Request object for an asynchronous receive.
     */
    public final class Recv_MPI_Request extends MPI_Request {
        /**
         * The destination buffer in the JVM.
         */
        private final double[] dst;
        /**
         * The offset in the destination buffer to receive at.
         */
        private final int offset;
        /**
         * An element count for the receive.
         */
        private final int count;

        /**
         * Constructor.
         *
         * @param setSize Size in bytes of the receive
         * @param setDst Destination JVM buffer
         * @param setOffset Offset into dst
         * @param setCount Number of elements in dst to receive
         */
        public Recv_MPI_Request(final long setSize, final double[] setDst,
                final int setOffset, final int setCount) {
            super(setSize);
            this.dst = setDst;
            this.offset = setOffset;
            this.count = setCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void complete() {
            buf.read(0, dst, offset, count);
        }
    }

    /**
     * An MPI_Request object for an asynchronous send.
     */
    public final class Send_MPI_Request extends MPI_Request {
        /**
         * Constructor.
         *
         * @param size Size in bytes of the receive
         */
        public Send_MPI_Request(final long size) {
            super(size);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void complete() {
        }
    }

    /**
     * Wrapper for the native pointer to an MPI_Status object.
     */
    public static final class MPI_Status {
        /**
         * Wrapped MPI_Status object.
         */
        protected final Pointer status;

        /**
         * Constructor.
         *
         * @param setStatus Wrapped MPI_Status object
         */
        public MPI_Status(final Pointer setStatus) {
            this.status = setStatus;
        }
    }

    /**
     * A Java exception wrapping an MPI error code, thrown in the case of some
     * MPI error.
     */
    public static final class MPIException extends Exception {
        /**
         * MPI Error code.
         */
        private final int code;

        /**
         * Constructor.
         *
         * @param setCode MPI error code
         */
        public MPIException(final int setCode) {
            this.code = setCode;
        }

        /**
         * Get the MPI error code.
         *
         * @return MPI error code
         */
        public int getErrCode() {
            return code;
        }
    }
}
