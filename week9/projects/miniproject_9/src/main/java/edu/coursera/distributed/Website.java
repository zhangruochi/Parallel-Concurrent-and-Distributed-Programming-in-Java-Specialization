package edu.coursera.distributed;

import java.util.LinkedList;
import java.util.Iterator;

import java.io.Serializable;

/**
 * A single website in a graph of websites.
 */
public final class Website implements Serializable {
    /**
     * Unique ID for this website.
     */
    private int id;
    /**
     * List of edges from this website (i.e. links to other websites),
     * represented by a list of the IDs for the destination sites.
     */
    private LinkedList<Integer> edges;

    /**
     * Constructor.
     *
     * @param setId ID of this website
     */
    public Website(final int setId) {
        this.id = setId;
        this.edges = new LinkedList<Integer>();
    }

    /**
     * Constructor, default initializes ID to -1.
     */
    public Website() {
        this(-1);
    }

    /**
     * Add an outbound edge from this website.
     *
     * @param target Destination of the new outbound edge
     */
    void addEdge(final int target) {
        edges.add(target);
    }

    /**
     * Get the unique ID of this website.
     *
     * @return ID of this site
     */
    int getId() {
        return id;
    }

    /**
     * Get the number of outbound edges from this website.
     *
     * @return Number of edges outbound from this site.
     */
    int getNEdges() {
        return edges.size();
    }

    /**
     * Get an iterator over edges outbound from this site, defined by the
     * integer IDs of the destination sites.
     *
     * @return Iterator over target site IDs of outbound edges.
     */
    Iterator<Integer> edgeIterator() {
        return edges.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Website) {
            Website other = (Website) o;
            return other.id == id;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id;
    }
}
