package com.kotula.nikolai.trainingpeakscodetest.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Immutable POJO for encapsulating the Peak Heart Rate data from the Workout REST result.
 */
public class PeakHeartRate implements Parcelable {
    public static final String PARCEL_PEAK_HEART_RATE = "com.kotula.nikolai.trainingpeakscodetest.data.PEAK_HEART_RATE";

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(begin);
        dest.writeLong(end);
        dest.writeInt(interval);
        dest.writeInt(value);
    }

    public static final Parcelable.Creator<PeakHeartRate> CREATOR =
            new Parcelable.Creator<PeakHeartRate>() {
                public PeakHeartRate createFromParcel(Parcel in) {
                    return new PeakHeartRate(in);
                }

                @Override
                public PeakHeartRate[] newArray(int size) {
                    return new PeakHeartRate[size];
                }
            };

    private PeakHeartRate(Parcel in) {
        begin = in.readLong();
        end = in.readLong();
        interval = in.readInt();
        value = in.readInt();
    }

    @Override
    public String toString() {
        String stringRep = "";

        stringRep += "Peak Heart Rate: ";
        stringRep += "begin: ";
        stringRep += begin;

        stringRep += " end: ";
        stringRep += end;

        stringRep += " interval: ";
        stringRep += interval;

        stringRep += " value: ";
        stringRep += value;

        return stringRep;
    }

    private boolean equals(PeakHeartRate other) {
        return (other.getBegin() == begin) &&
                (other.getEnd() == end) &&
                (other.getInterval() == interval) &&
                (other.getValue() == value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PeakHeartRate))
            return false;

        return this.equals((PeakHeartRate) obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end, interval, value);
    }
}
