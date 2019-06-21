package edu.coursera.concurrent;

/**
 * Represents a single account in a bank whose only contents is the balance of
 * that account.
 */
public final class Account {
    /**
     * Unique ID number for this account.
     */
    private int id;

    /**
     * Current balance of this account.
     */
    private int balance;

    /**
     * Constructor.
     *
     * @param setId The ID for this account
     * @param setStartingBalance The initial balance for this account.
     */
    public Account(final int setId, final int setStartingBalance) {
        this.id = setId;
        this.balance = setStartingBalance;
    }

    /**
     * Get the remaining balance in this account.
     *
     * @return The current balance.
     */
    public int balance() {
        return balance;
    }

    /**
     * Remove the specified amount from the current balance of the account.
     *
     * @param amount The amount to subtract from the current balance.
     * @return true if it was possible to subtract that amount, false otherwise.
     */
    public boolean withdraw(final int amount) {
        if (amount > 0 && amount < balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Add the specified amount to the current balance.
     *
     * @param amount The amount to add.
     * @return true if it was possible to add that amount (i.e. amount > 0),
     *         false otherwise.
     */
    public boolean deposit(final int amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    /**
     * A dummy busy work function for simulating more work to increase isolated
     * contention. While this work is artificial in this setting, it serves to
     * more accurately simulate real-world conditions where a bank transfer is
     * not a shared-memory operation (i.e., it must contend with network
     * latencies, disk latencies, etc.).
     *
     * @param srcID The source account ID
     * @param destID The destination account ID
     */
    private static void busyWork(final int srcID, final int destID) {
        for (int i = 0; i < srcID * 100; i++) {
            ;
        }
        for (int i = 0; i < destID * 100; i++) {
            ;
        }
    }

    /**
     * Transfer the specified amount from this account to the specified target
     * account.
     *
     * @param amount The amount to transfer.
     * @param target The destination of this transfer.
     * @return true if the transfer is successful, false otherwise.
     */
    public boolean performTransfer(final int amount, final Account target) {
        final boolean withdrawSuccess = withdraw(amount);
        busyWork(this.id, target.id);
        if (withdrawSuccess) {
            target.deposit(amount);
        }
        return withdrawSuccess;
    }
}
