package edu.coursera.concurrent;

import java.util.Random;

import junit.framework.TestCase;

import static edu.rice.pcdp.PCDP.finish;
import static edu.rice.pcdp.PCDP.async;

public class BankTransactionsTest extends TestCase {
    private final static int numAccounts = 3_000;
    private final static int numTransactions = 800_000;

    private static int getNCores() {
        String ncoresStr = System.getenv("COURSERA_GRADER_NCORES");
        if (ncoresStr == null) {
            return Runtime.getRuntime().availableProcessors();
        } else {
            return Integer.parseInt(ncoresStr);
        }
    }

    private static int randomIntValue(final Random random, final int limit) {
        return (int) (Math.abs(random.nextDouble() * 10000) % limit);
    }

    private static long sumBalances(final Account[] bankAccounts) {
        long res = 0;
        for (final Account bankAccount : bankAccounts) {
            res += bankAccount.balance();
        }
        return res;
    }

    private static long testDriver(final ThreadSafeBankTransaction impl) {
        final Account[] bankAccounts = new Account[numAccounts];
        for (int i = 0; i < numAccounts; i++) {
            bankAccounts[i] = new Account(i,
                    1000 * (randomIntValue(new Random(1000), numAccounts) + 1));
        }

        final long preSumOfBalances = sumBalances(bankAccounts);
        final long startTime = System.currentTimeMillis();
        finish(() -> {
            for (int i = 0; i < numTransactions; i++) {
                final int ii = i;

                async(() -> {
                    final Random myRandom = new Random(100L * (ii + 1));

                    final int srcIndex = randomIntValue(myRandom, numAccounts);
                    final Account srcAccount = bankAccounts[srcIndex];

                    final int destIndex = randomIntValue(myRandom, numAccounts);
                    final Account destAccount = bankAccounts[destIndex];

                    final int transferAmount = randomIntValue(myRandom,
                        srcAccount.balance());

                    impl.issueTransfer(transferAmount, srcAccount, destAccount);
                });
            }
        });
        final long elapsed = System.currentTimeMillis() - startTime;

        System.out.println(impl.getClass().getSimpleName() + ": Performed " +
                numTransactions + " transactions with " + numAccounts +
                " accounts and " + getNCores() + " threads, in " + elapsed +
                " ms");

        final long postSumOfBalances = sumBalances(bankAccounts);
        assertTrue("Expected total balance before and after simulation to be " +
                "equal, but was " + preSumOfBalances + " before and " +
                postSumOfBalances + " after",
                preSumOfBalances == postSumOfBalances);

        return elapsed;
    }

    public void testObjectIsolation() {
        // warmup
        testDriver(new BankTransactionsUsingGlobalIsolation());
        final long globalTime = testDriver(
                new BankTransactionsUsingGlobalIsolation());

        // warmup
        testDriver(new BankTransactionsUsingObjectIsolation());
        final long objectTime = testDriver(
                new BankTransactionsUsingObjectIsolation());
        final double improvement = (double)globalTime / (double)objectTime;

        final int ncores = getNCores();
        double expected;
        if (ncores == 2) {
            expected = 1.4;
        } else if (ncores == 4) {
            expected = 2.2;
        } else {
            expected = 0.7 * ncores;
        }
        final String msg = String.format("Expected an improvement of at " +
                "least %fx with object-based isolation, but saw %fx", expected,
                improvement);
        assertTrue(msg, improvement >= expected);
    }
}
