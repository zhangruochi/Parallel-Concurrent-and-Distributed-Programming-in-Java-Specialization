package edu.coursera.concurrent;

/**
 * An abstract interface for all thread-safe bank transaction implementations to
 * extend.
 */
public abstract class ThreadSafeBankTransaction {
    /**
     * Transfer the specified amount from src to dst.
     *
     * @param amount Amount to transfer
     * @param src Source account
     * @param dst Destination account
     */
    public abstract void issueTransfer(final int amount, final Account src,
            final Account dst);
}
