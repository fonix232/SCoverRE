package org.apache.commons.lang.math;

import java.util.Random;

public final class JVMRandom extends Random {
    private static final long serialVersionUID = 1;
    private boolean constructed;

    public JVMRandom() {
        this.constructed = false;
        this.constructed = true;
    }

    public synchronized void setSeed(long seed) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }

    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }

    public void nextBytes(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }

    public int nextInt() {
        return nextInt(Integer.MAX_VALUE);
    }

    public int nextInt(int n) {
        if (n > 0) {
            return (int) (Math.random() * ((double) n));
        }
        throw new IllegalArgumentException("Upper bound for nextInt must be positive");
    }

    public long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    public static long nextLong(long n) {
        if (n > 0) {
            return (long) (Math.random() * ((double) n));
        }
        throw new IllegalArgumentException("Upper bound for nextInt must be positive");
    }

    public boolean nextBoolean() {
        return Math.random() > 0.5d;
    }

    public float nextFloat() {
        return (float) Math.random();
    }

    public double nextDouble() {
        return Math.random();
    }
}
