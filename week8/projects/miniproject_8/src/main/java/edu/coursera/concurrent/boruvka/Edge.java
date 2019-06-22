package edu.coursera.concurrent.boruvka;

/**
 * An abstract representation of a weighted, directed edge in a graph connecting
 * two components.
 *
 * @param <C> Component type that this edge connects.
 */
public abstract class Edge<C extends Component> {
    /**
     * Fetch the weight of this edge.
     *
     * @return Weight of this edge.
     */
    public abstract double weight();

    /**
     * Given one member of this edge, swap it out for a different component.
     *
     * @param from The component already in this edge
     * @param to The component to replace with
     * @return this
     */
    public abstract Edge<C> replaceComponent(final C from, final C to);

    /**
     * Source component for this edge.
     *
     * @return Source component for this edge.
     */
    public abstract C fromComponent();

    /**
     * Destination component for this edge.
     *
     * @return Destination component for this edge.
     */
    public abstract C toComponent();

    /**
     * Given one member of this edge, return the other.
     *
     * @param component One component of this edge.
     * @return The other component of this edge.
     */
    public abstract C getOther(final C component);
}
