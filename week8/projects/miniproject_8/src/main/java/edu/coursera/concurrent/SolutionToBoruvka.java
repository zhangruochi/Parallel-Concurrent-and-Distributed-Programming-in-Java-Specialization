package edu.coursera.concurrent;

import edu.coursera.concurrent.boruvka.Component;

/**
 * A class for storing the solution component of Boruvka's algorithm in, for
 * testing.
 *
 * @param <C> Type of component used in this run.
 */
public final class SolutionToBoruvka<C extends Component> {
    /**
     * The final component the graph collapses down to.
     */
    private C solution = null;

    /**
     * Provide a solution to the testing code. Single assignment.
     *
     * @param setSolution Computed solution.
     */
    public void setSolution(final C setSolution) {
        assert (solution == null);
        assert (setSolution != null);
        this.solution = setSolution;
    }

    /**
     * Get the provided solution.
     *
     * @return Computed solution.
     */
    public C getSolution() {
        return solution;
    }
}
