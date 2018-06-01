package com.kotula.nikolai.trainingpeakscodetest.data;

/**
 * Immutable POJO for encapsulating the Peak Speed data from the Workout REST result.
 */
public class PeakSpeed {
    private long begin;
    private long end;
    private int interval;
    private double value;

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    public int getInterval() {
        return interval;
    }

    public double getValue() {
        return value;
    }

    public PeakSpeed(
            long begin,
            long end,
            int interval,
            double value) {
        this.begin = begin;
        this.end = end;
        this.interval = interval;
        this.value = value;
    }
}
