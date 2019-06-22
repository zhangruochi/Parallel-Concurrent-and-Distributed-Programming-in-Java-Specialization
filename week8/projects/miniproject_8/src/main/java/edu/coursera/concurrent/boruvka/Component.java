package edu.coursera.concurrent.boruvka;

/**
 * An abstract definition of a component in a graph, which may be a singleton
 * node or may be the result of merging multiple nodes.
 *
 * @param <C> Type of this component.
 */
public abstract class Component<C extends Component> {
    /**
     * Fetch the uniqe node ID for this node in the graph.
     *
     * @return Node ID
     */
    public abstract int nodeId();

    /**
     * Add an edge to this component.
     *
     * @param e Edge to add, which is anchored on this component.
     */
    public abstract void addEdge(final Edge<C> e);

    /**
     * Compute the total weight of this component.
     *
     * @return The weight of this component, based on the number of edges that
     *         have been collapsed to form it.
     */
    public abstract double totalWeight();

    /**
     * Compute the number of edges that have been collapsed to form this
     * component.
     *
     * @return # edges collapsed into this component.
     */
    public abstract long totalEdges();
}
