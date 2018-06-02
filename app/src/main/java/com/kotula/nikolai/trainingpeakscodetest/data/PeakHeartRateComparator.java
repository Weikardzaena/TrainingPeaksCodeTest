package com.kotula.nikolai.trainingpeakscodetest.data;

import java.util.Comparator;

public class PeakHeartRateComparator implements Comparator<PeakHeartRate> {
    @Override
    public int compare(PeakHeartRate o1, PeakHeartRate o2) {
        return o1.getInterval() - o2.getInterval();
    }
}
