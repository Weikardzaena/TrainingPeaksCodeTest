package com.kotula.nikolai.trainingpeakscodetest.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Immutable POJO for encapsulating the Peak Speed data from the Workout REST result.
 */
public class PeakSpeed implements Parcelable {
    public static final String PARCEL_PEAK_SPEED = "com.kotula.nikolai.trainingpeakscodetest.data.PEAK_SPEED";

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(begin);
        dest.writeLong(end);
        dest.writeInt(interval);
        dest.writeDouble(value);
    }

    public static final Parcelable.Creator<PeakSpeed> CREATOR =
            new Parcelable.Creator<PeakSpeed>() {
                public PeakSpeed createFromParcel(Parcel in) {
                    return new PeakSpeed(in);
                }

                @Override
                public PeakSpeed[] newArray(int size) {
                    return new PeakSpeed[size];
                }
            };

    private PeakSpeed(Parcel in) {
        begin = in.readLong();
        end = in.readLong();
        interval = in.readInt();
        value = in.readDouble();
    }

    @Override
    public String toString() {
        final String lineSeparator = System.getProperty("line.separator");

        StringBuilder builder = new StringBuilder();
        builder.append("Peak Speed:");
        builder.append(lineSeparator);
        builder.append(lineSeparator);

        builder.append("begin: ");
        builder.append(begin);
        builder.append(lineSeparator);

        builder.append("end: ");
        builder.append(end);
        builder.append(lineSeparator);

        builder.append("interval: ");
        builder.append(interval);
        builder.append(lineSeparator);

        builder.append("value: ");
        builder.append(value);
        builder.append(lineSeparator);

        return builder.toString();
    }
}
