package edu.coursera.concurrent;

/**
 *  Class SyncList implements a thread-safe sorted list data structure that
 *  supports contains(), add() and remove() methods.
 *
 *  Thread safety is guaranteed by declaring each of the methods to be
 *  synchronized.
 */
public final class SyncList extends ListSet {
    /**
     * Constructor.
     */
    public SyncList() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean contains(final Integer object) {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
            pred = curr;
            curr = curr.next;
        }
        return object.equals(curr.object);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean add(final Integer object) {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
            pred = curr;
            curr = curr.next;
        }

        if (object.equals(curr.object)) {
            return false;
        } else {
            final Entry entry = new Entry(object);
            entry.next = curr;
            pred.next = entry;
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean remove(final Integer object) {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
            pred = curr;
            curr = curr.next;
        }

        if (object.equals(curr.object)) {
            pred.next = curr.next;
            return true;
        } else {
            return false;
        }
    }
}
