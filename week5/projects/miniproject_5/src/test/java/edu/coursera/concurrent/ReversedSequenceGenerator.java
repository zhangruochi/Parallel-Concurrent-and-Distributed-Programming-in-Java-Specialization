package edu.coursera.concurrent;

public class ReversedSequenceGenerator implements SequenceGenerator {
    private final SequenceGenerator seq;
    private int[] buffered;
    private int iter;

    public ReversedSequenceGenerator(final SequenceGenerator setSeq) {
        this.seq = setSeq;
        this.buffered = new int[this.seq.sequenceLength()];
        for (int i = buffered.length - 1; i >= 0; i--) {
            buffered[i] = seq.next();
        }
        this.iter = 0;
    }

    @Override
    public int sequenceLength() {
        return seq.sequenceLength();
    }

    @Override
    public int next() {
        return buffered[iter++];
    }

    @Override
    public void reset() {
        this.seq.reset();
        this.buffered = new int[this.seq.sequenceLength()];
        for (int i = buffered.length - 1; i >= 0; i--) {
            this.buffered[i] = seq.next();
        }
        this.iter = 0;
    }

    @Override
    public String getLabel() {
        return "Reversed " + seq.getLabel();
    }
}
