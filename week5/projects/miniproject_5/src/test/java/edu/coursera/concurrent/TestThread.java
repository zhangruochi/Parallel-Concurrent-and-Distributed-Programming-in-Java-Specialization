package edu.coursera.concurrent;

import java.util.Random;

/*
 * An abstract interface to be implemented by all testing threads.
 */
public abstract class TestThread {
    // The ListSet to operate on
    protected ListSet l;

    // The values to pass to the target list
    protected final Integer[] nums;

    public TestThread(final SequenceGenerator seq, final int seqToUse,
            final ListSet setL) {
        this.nums = new Integer[seqToUse];
        for (int i = 0; i < this.nums.length; i++) {
            this.nums[i] = new Integer(seq.next());
        }
        this.l = setL;
    }
}
