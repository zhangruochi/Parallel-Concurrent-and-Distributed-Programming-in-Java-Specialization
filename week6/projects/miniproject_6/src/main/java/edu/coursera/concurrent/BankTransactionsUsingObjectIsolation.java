package edu.coursera.concurrent;

import static edu.rice.pcdp.PCDP.isolated;

/**
 * A thread-safe transaction implementation using object-based isolation.
 */
public final class BankTransactionsUsingObjectIsolation
        extends ThreadSafeBankTransaction {
    /**
     * {@inheritDoc}
     */
    @Override
    public void issueTransfer(final int amount, final Account src,
            final Account dst) {
        /*
         * TODO implement issueTransfer using object-based isolation instead of
         * global isolation, based on the reference code provided in
         * BankTransactionsUsingGlobalIsolation. Keep in mind that isolation
         * must be applied to both src and dst.
         */
        isolated(src,dst,() -> {
            src.performTransfer(amount, dst);
        });
    }
}
