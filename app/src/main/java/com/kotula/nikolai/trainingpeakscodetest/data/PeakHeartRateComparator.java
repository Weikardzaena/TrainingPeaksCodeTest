package com.kotula.nikolai.trainingpeakscodetest.data;

import java.util.Comparator;

/**
 * A {@link Comparator} wrapper class for the {@link PeakHeartRate} POJO.
 */
public class PeakHeartRateComparator implements Comparator<PeakHeartRate> {
    @Override
    public int compare(PeakHeartRate o1, PeakHeartRate o2) {
        if (o1 == null) {
            if (o2 == null) {
                // compare(null, null) should return equality (zero).
                return 0;
            } else {
                // compare(null, <value>): 'null' should come after everything.
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
