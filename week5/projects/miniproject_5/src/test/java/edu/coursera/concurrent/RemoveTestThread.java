package edu.coursera.concurrent;

import java.util.Random;

/**
 * A Runnable class used to test the performance of each concurrent ListSet
 * implementation. This thread simply hits the list with a large number of
 * remove.
 */
public class RemoveTestThread extends TestThread implements Runnable {
    private int nSuccessful = 0;
    private int nFailed = 0;

    public RemoveTestThread(final SequenceGenerator seq, final int seqToUse, final ListSet setL) {
        super(seq, seqToUse, setL);
    }

    @Override
    public void run() {
        for (int i = 0; i < nums.length; i++) {
            if (l.remove(nums[i])) {
                nSuccessful++;
            } else {
                nFailed++;
            }
        }
    }

    public int getNSuccessful() {
        return nSuccessful;
    }

    public int getNFailed() {
        return nFailed;
    }
}
