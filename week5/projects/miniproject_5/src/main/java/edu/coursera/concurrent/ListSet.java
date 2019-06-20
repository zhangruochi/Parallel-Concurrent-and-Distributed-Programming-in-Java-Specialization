package edu.coursera.concurrent;

/**
 * The abstract interface implemented by each of the List versions tested in
 * this mini-project. Lists that support this interface must be able to add
 * objects, remove objects, and test for existence of an object. These methods
 * are required to maintain a sorted list of items internally with no
 * duplicates.
 */
public abstract class ListSet {
    /**
     * Starting entry of this concurrent list.
     */
    protected final Entry head;

    /**
     * Default constructor.
     */
    public ListSet() {
        this.head = new Entry(Integer.MIN_VALUE);
        this.head.next = new Entry(Integer.MAX_VALUE);
    }

    /**
     * Getter for the head of the list.
     *
     * @return The head of this list.
     */
    public Entry getHead() {
        return head;
    }

    /**
     * Add an integer value to this sorted list, ensuring uniqueness. This
     * method must use ListSet.head as the head of the list.
     *
     * @param o The integer to add.
     * @return false if this value already exists in the list, true otherwise
     */
    abstract boolean add(Integer o);

    /**
     * Remove an integer value from this list if it exists. This method must use
     * ListSet.head as the head of the list.
     *
     * @param o The integer to remove.
     * @return true if this value is found in the list and successfully removed,
     *         false otherwise
     */
    abstract boolean remove(Integer o);

    /**
     * Check if this list contains the provided value. This method must use
     * ListSet.head as the head of the list.
     *
     * @param o The integer to check for.
     * @return true if this list contains the target value, false otherwise.
     */
    abstract boolean contains(Integer o);
}
