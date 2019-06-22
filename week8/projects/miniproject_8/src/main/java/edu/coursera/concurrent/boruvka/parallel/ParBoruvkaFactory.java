package edu.coursera.concurrent.boruvka.parallel;

import edu.coursera.concurrent.boruvka.BoruvkaFactory;

import edu.coursera.concurrent.ParBoruvka.ParComponent;
import edu.coursera.concurrent.ParBoruvka.ParEdge;

/**
 * A factory for generating components and edges when performing a parallel
 * traversal.
 */
public final class ParBoruvkaFactory
        implements BoruvkaFactory<ParComponent, ParEdge> {
    /**
     * {@inheritDoc}
     */
    @Override
    public ParComponent newComponent(final int nodeId) {
        return new ParComponent(nodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParEdge newEdge(final ParComponent from, final ParComponent to,
            final double weight) {
        return new ParEdge(from, to, weight);
    }
}
