package edu.coursera.concurrent;

/**
 * An abstract class that exposes a single countPrimes interface for testing.
 */
public abstract class Sieve {
    /**
     * Counts the number of prime numbers <= the provided limit using the Sieve
     * of Eratosthenes.
     *
     * @param limit Only find primes less than or equal to this limit.
     * @return The number of primes that are <= limit.
     */
    public abstract int countPrimes(final int limit);
}
