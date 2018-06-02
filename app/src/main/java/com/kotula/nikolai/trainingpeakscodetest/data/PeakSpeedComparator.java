package com.kotula.nikolai.trainingpeakscodetest.data;

import java.util.Comparator;

public class PeakSpeedComparator implements Comparator<PeakSpeed> {
    @Override
    public int compare(PeakSpeed o1, PeakSpeed o2) {
        return o1.getInterval() - o2.getInterval();
    }
}
