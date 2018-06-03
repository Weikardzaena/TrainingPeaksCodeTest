package com.kotula.nikolai.trainingpeakscodetest.data;

import java.util.Comparator;

/**
 * A {@link Comparator} wrapper class for the {@link PeakSpeed} POJO.
 */
public class PeakSpeedComparator implements Comparator<PeakSpeed> {
    @Override
    public int compare(PeakSpeed o1, PeakSpeed o2) {
        if (o1 == null) {
            if (o2 == null) {
                // compare(null, null) should return 0.
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
