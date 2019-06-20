package edu.coursera.concurrent;

/**
 * A single element in any of the list implementations.
 */
public final class Entry {
    /**
     * The value stored in this list entry.
     */
    public final Integer object;

    /**
     * The next element in this singly linked list.
     */
    public Entry next;

    /**
     * The general constructor used when creating a new list entry.
     *
     * @param setObject Value to store in this item
     */
    Entry(final Integer setObject) {
        this.object = setObject;
    }
}
