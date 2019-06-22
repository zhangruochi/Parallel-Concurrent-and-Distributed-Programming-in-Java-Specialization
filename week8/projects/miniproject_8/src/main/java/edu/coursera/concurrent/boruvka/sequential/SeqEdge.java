package edu.coursera.concurrent.boruvka.sequential;

import edu.coursera.concurrent.boruvka.Edge;

/**
 * An edge class used in the sequential Boruvka implementation.
 */
public final class SeqEdge extends Edge<SeqComponent>
        implements Comparable<Edge> {

    /**
     * Source component.
     */
    protected SeqComponent fromComponent;
    /**
     * Destination component.
     */
    protected SeqComponent toComponent;
    /**
     * Weight of this edge.
     */
    public double weight;

    /**
     * Constructor.
     *
     * @param from From edge.
     * @param to To edges.
     * @param w Weight of this edge.
     */
    protected SeqEdge(final SeqComponent from, final SeqComponent to,
            final double w) {
        fromComponent = from;
        toComponent = to;
        weight = w;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeqComponent fromComponent() {
        return fromComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeqComponent toComponent() {
        return toComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double weight() {
        return weight;
    }

    /**
     * {@inheritDoc}
     */
    public SeqComponent getOther(final SeqComponent from) {
        if (fromComponent == from) {
            assert (toComponent != from);
            return toComponent;
        }

        if (toComponent == from) {
            assert (fromComponent != from);
            return fromComponent;
        }
        assert (false);
        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Edge e) {
        if (e.weight() == weight) {
            return 0;
        } else if (weight < e.weight()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * {@inheritDoc}
     */
    public SeqEdge replaceComponent(final SeqComponent from,
            final SeqComponent to) {
        if (fromComponent == from) {
            fromComponent = to;
        }
        if (toComponent == from) {
            toComponent = to;
        }
        return this;
    }
}
