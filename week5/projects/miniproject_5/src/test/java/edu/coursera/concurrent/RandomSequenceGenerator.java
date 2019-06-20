package edu.coursera.concurrent;

import java.util.Random;

public class RandomSequenceGenerator implements SequenceGenerator {
    private final int seed;
    private Random rand;
    private final int maxNum;
    private final int sequenceLen;

    public RandomSequenceGenerator(final int setSeed,
            final int setSequenceLen, final int setMaxNum) {
        this.seed = setSeed;
        this.rand = new Random(this.seed);
        this.maxNum = setMaxNum;
        this.sequenceLen = setSequenceLen;
    }

    @Override
    public int sequenceLength() {
        return sequenceLen;
    }

    @Override
    public int next() {
        return rand.nextInt(maxNum);
    }

    @Override
    public void reset() {
        this.rand = new Random(this.seed);
    }

    @Override
    public String getLabel() {
        return "Random";
    }
}
