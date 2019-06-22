package edu.coursera.concurrent.boruvka.sequential;

import edu.coursera.concurrent.boruvka.BoruvkaFactory;

/**
 * A factory for generating components and edges when performing a sequential
 * traversal.
 */
public final class SeqBoruvkaFactory
        implements BoruvkaFactory<SeqComponent, SeqEdge> {
    /**
     * {@inheritDoc}
     */
    @Override
    public SeqComponent newComponent(final int nodeId) {
        return new SeqComponent(nodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SeqEdge newEdge(final SeqComponent from, final SeqComponent to,
            final double weight) {
        return new SeqEdge(from, to, weight);
    }
}
