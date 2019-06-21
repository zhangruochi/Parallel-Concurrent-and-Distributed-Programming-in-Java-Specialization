package edu.coursera.concurrent;

import static edu.rice.pcdp.PCDP.isolated;

/**
 * A thread-safe transaction implementation using global isolation.
 */
public final class BankTransactionsUsingGlobalIsolation
        extends ThreadSafeBankTransaction {
    /**
     * {@inheritDoc}
     */
    @Override
    public void issueTransfer(final int amount, final Account src,
            final Account dst) {
        isolated(() -> {
            src.performTransfer(amount, dst);
        });
    }
}
