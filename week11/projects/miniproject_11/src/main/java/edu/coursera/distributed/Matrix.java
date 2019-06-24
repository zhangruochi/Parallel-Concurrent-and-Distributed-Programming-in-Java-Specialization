package edu.coursera.distributed;

/**
 * Represents a single two-dimensional matrix.
 */
public final class Matrix {
    /**
     * The values of the matrix, flattened and stored in row-major order.
     */
    private final double[] values;
    /**
     * The number of rows in the matrix.
     */
    private final int nrows;
    /**
     * The number of columns in the matrix.
     */
    private final int ncols;

    /**
     * Constructor.
     *
     * @param setNrows Number of rows
     * @param setNcols Number of columns
     */
    public Matrix(final int setNrows, final int setNcols) {
        this.nrows = setNrows;
        this.ncols = setNcols;
        this.values = new double[nrows * ncols];
    }

    /**
     * Copy constructor.
     *
     * @param other Another matrix to copy the contents of
     */
    public Matrix(final Matrix other) {
        this.nrows = other.nrows;
        this.ncols = other.ncols;
        this.values = new double[nrows * ncols];
        System.arraycopy(other.values, 0, values, 0, values.length);
    }

    /**
     * Set the value at (row, col) to the specified value.
     *
     * @param row Row index
     * @param col Column index
     * @param val Value
     */
    public void set(final int row, final int col, final double val) {
        values[row * ncols + col] = val;
    }

    /**
     * Increment the value at (row, col) by the specified value.
     *
     * @param row Row index
     * @param col Column index
     * @param val Increment value
     */
    public void incr(final int row, final int col, final double val) {
        values[row * ncols + col] += val;
    }

    /**
     * Fetch the value currently stored at (row, col).
     *
     * @param row Row index
     * @param col Column index
     * @return The value stored at (row, col)
     */
    public double get(final int row, final int col) {
        return values[row * ncols + col];
    }

    /**
     * Fetch the number of rows in this matrix.
     *
     * @return # of rows
     */
    public int getNRows() {
        return nrows;
    }
    
    /**
     * Fetch the number of columns in this matrix.
     *
     * @return # of columns
     */
    public int getNCols() {
        return ncols;
    }

    /**
     * Get the offset in the flattened values array of the first element in the
     * specified row.
     *
     * @param row Row to get the offset of
     * @return Row offset
     */
    public int getOffsetOfRow(final int row) {
        return row * ncols;
    }

    /**
     * Get the raw, one-dimensional values array used to store this matrices
     * values.
     *
     * @return Raw values array
     */
    public double[] getValues() {
        return values;
    }
}
