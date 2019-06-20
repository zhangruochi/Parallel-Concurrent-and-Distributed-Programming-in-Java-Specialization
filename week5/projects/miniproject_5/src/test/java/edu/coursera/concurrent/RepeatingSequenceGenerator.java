package edu.coursera.concurrent;

public class RepeatingSequenceGenerator implements SequenceGenerator {
    private final int[] subsequence;
    private int iter;
    private final int sequenceLen;

    public RepeatingSequenceGenerator(final int setSequenceLen,
            final int periodicity) {
        this.subsequence = new int[periodicity];
        this.iter = 0;
        this.sequenceLen = setSequenceLen;

        for (int i = 0; i < this.subsequence.length; i++) {
            this.subsequence[i] = i;
        }
    }

    @Override
    public int sequenceLength() {
        return sequenceLen;
    }

    @Override
    public int next() {
        return subsequence[(iter++) % subsequence.length];
    }

    @Override
    public void reset() {
        this.iter = 0;
    }

    @Override
    public String getLabel() {
        return "Repeating";
    }
}
