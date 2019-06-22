package edu.coursera.concurrent;

import edu.coursera.concurrent.boruvka.Component;

import java.util.Queue;

/**
 * An abstract interface of the APIs to test for computing a spanning tree using
 * Boruvka's algorithm.
 *
 * @param <C> Type of component this Boruvka implementation operates on.
 */
public abstract class AbstractBoruvka<C extends Component> {
    /**
     * Given a queue of nodes making up the initial graph, compute a minimal
     * spanning tree and return the result in the provided solution object.
     *
     * For SeqBoruvka, this method is called single-threaded and passed
     * nodesLoaded as a LinkedList.
     *
     * However, for ParBoruvka this method is called from multiple threads at
     * the same time and passed nodesLoaded as a ConcurrentLinkedQueue. Hence,
     * the programmer is essentially handed their thread pool and a thread-safe
     * work pool at the start of execution, and can safely grab work from that
     * work pool from any thread. However, synchronization is still required on
     * the graph itself, just not on the input work queue.
     *
     * @param nodesLoaded A queue of all nodes in the input graph. For
     *                    SeqBoruvka, this is guaranteed to be a LinkedList. For
     *                    ParBoruvka, this is guaranteed to be a
     *                    ConcurrentLinkedQueue (i.e. you may perform operations
     *                    on it from multiple threads safely).
     * @param solution An object for storing the final component the graph
     *                 collapsed down to using Boruvka's algorithm. *** At the
     *                 completion of your kernel, call setSolution on this
     *                 object to return your result to the testing code ***
     */
    public abstract void computeBoruvka(final Queue<C> nodesLoaded,
            final SolutionToBoruvka<C> solution);
}
