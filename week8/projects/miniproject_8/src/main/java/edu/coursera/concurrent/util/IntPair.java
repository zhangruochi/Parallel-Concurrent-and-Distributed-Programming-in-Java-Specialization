package edu.coursera.concurrent.util;

/**
 * A class to encapsulate a pair of integers, representing an edge in a graph.
 */
public final class IntPair {
    /**
     * One item in the pair.
     */
    public final int left;
    /**
     * One item in the pair.
     */
    public final int right;

    /**
     * Constructor.
     *
     * @param f One item in pair
     * @param s One item in pair
     */
    public IntPair(final int f, final int s) {
        left = f;
        right = s;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass())) {
            return false;
        }
        final IntPair p = (IntPair) o;
        return p.left == left && p.right == right;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 13;
        hash = (31 * hash) + left;
        hash = (31 * hash) + right;
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("<%d,%d>", left, right);
    }
}
