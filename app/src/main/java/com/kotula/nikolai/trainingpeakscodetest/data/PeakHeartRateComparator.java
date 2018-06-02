package com.kotula.nikolai.trainingpeakscodetest.data;

import java.util.Comparator;

public class PeakHeartRateComparator implements Comparator<PeakHeartRate> {
    @Override
    public int compare(PeakHeartRate o1, PeakHeartRate o2) {
        if (o1 == null) {
            if (o2 == null) {
                // compare(null, null) should return 0.
                return 0;
            } else {
                // compare(null, <value>) means
                return 1;
            }
        } else if (o2 == null) {
            // compare(null, null) has already been checked.
            return -1;
        }
        if (o1.getInterval() > o2.getInterval()) {
            return 1;
        } else if (o1.getInterval() < o2.getInterval()) {
            return -1;
        } else {
            return 0;
        }
    }
}
