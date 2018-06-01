package com.kotula.nikolai.trainingpeakscodetest.data;

/**
 * Immutable POJO for encapsulating the Peak Heart Rate data from the Workout REST result.
 */
public class PeakHeartRate {
    private long begin;
    private long end;
    private int interval;
    private int value;

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    public int getInterval() {
        return interval;
    }

    public int getValue() {
        return value;
    }

    public PeakHeartRate(
            long begin,
            long end,
            int interval,
            int value) {
        this.begin = begin;
        this.end = end;
        this.interval = interval;
        this.value = value;
    }
}
