package com.kotula.nikolai.trainingpeakscodetest.repos.concrete;

import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.ResultCode;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnitTestWorkoutRepo implements IWorkoutRepo {
    private static final String TAG = "UnitTestWorkoutRepo";

    /**
     * Fetches a {@link List} of {@link PeakHeartRate} objects from the data source.
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @param outData The {@link List} of {@link PeakHeartRate} objects that will be filled with the fetched data.
     * @return A code indicating the result of the operation.
     */
    @Override
    public ResultCode getPeakHeartRates(String workoutTag, List<PeakHeartRate> outData) {
        Log.d(TAG, "getPeakSpeeds()");

        ArrayList<PeakHeartRate> heartRates = new ArrayList<>();

        // Negative values:
        heartRates.add(new PeakHeartRate(-1, -10, -100, -1000));
        heartRates.add(new PeakHeartRate(-10, 123, -189, 8389));
        heartRates.add(new PeakHeartRate(10, -123, 288, -55));
        heartRates.add(new PeakHeartRate(99, -18, -789, 8999));
        heartRates.add(new PeakHeartRate(105, -443, -974, -563));

        // Extremes
        heartRates.add(new PeakHeartRate(10213901283l, 192873981273l, 9871791, 478491819));
        heartRates.add(new PeakHeartRate(Long.MAX_VALUE, Long.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
        heartRates.add(new PeakHeartRate(Long.MIN_VALUE, Long.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE));
        heartRates.add(new PeakHeartRate(0, 0, 0, 0));

        // Duplicates
        heartRates.add(new PeakHeartRate(1,1,1,1));
        heartRates.add(new PeakHeartRate(1,1,1,1));
        heartRates.add(new PeakHeartRate(1,1,1,1));

        // Almost Duplicates
        heartRates.add(new PeakHeartRate(1,1,1,2));
        heartRates.add(new PeakHeartRate(1,1,2,1));
        heartRates.add(new PeakHeartRate(1,2,1,1));
        heartRates.add(new PeakHeartRate(2,1,1,1));

        // Several null values just because.
        heartRates.add(null);
        heartRates.add(null);

        // The ending order should always be the same.  This tests for that:
        Collections.shuffle(heartRates);
        outData.addAll(heartRates);
        return ResultCode.SUCCESS;
    }

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @param outData The {@link List} of {@link PeakSpeed} objects that will be filled with the fetched data.
     * @return A code indicating the result of the operation.
     */
    @Override
    public ResultCode getPeakSpeeds(String workoutTag, List<PeakSpeed> outData) {
        Log.d(TAG, "getPeakSpeeds()");
        ArrayList<PeakSpeed> speeds = new ArrayList<>();

        // Negative values:
        speeds.add(new PeakSpeed(-1, -10, -100, -1000.982374987329));
        speeds.add(new PeakSpeed(-10, 123, -189, 8389.98127932178912));
        speeds.add(new PeakSpeed(10, -123, 288, -55.0981273012093));
        speeds.add(new PeakSpeed(99, -18, -789, 8999.9081723082120));
        speeds.add(new PeakSpeed(105, -443, -974, -563.182730812));

        // Extremes
        speeds.add(new PeakSpeed(10213901283l, 192873981273l, 9871791, 478491819.802739802170));
        speeds.add(new PeakSpeed(Long.MAX_VALUE, Long.MAX_VALUE, Integer.MAX_VALUE, Double.MAX_VALUE));
        speeds.add(new PeakSpeed(Long.MIN_VALUE, Long.MIN_VALUE, Integer.MIN_VALUE, Double.MIN_VALUE));
        speeds.add(new PeakSpeed(Long.MIN_VALUE, Long.MIN_VALUE, Integer.MIN_VALUE, Double.POSITIVE_INFINITY));
        speeds.add(new PeakSpeed(Long.MIN_VALUE, Long.MIN_VALUE, Integer.MIN_VALUE, Double.NEGATIVE_INFINITY));
        speeds.add(new PeakSpeed(0, 0, 0, 0.0));

        // Duplicates
        speeds.add(new PeakSpeed(1,1,1,1));
        speeds.add(new PeakSpeed(1,1,1,1));
        speeds.add(new PeakSpeed(1,1,1,1));

        // Almost Duplicates
        speeds.add(new PeakSpeed(1,1,1,2));
        speeds.add(new PeakSpeed(1,1,2,1));
        speeds.add(new PeakSpeed(1,2,1,1));
        speeds.add(new PeakSpeed(2,1,1,1));

        // Several null values just because.
        speeds.add(null);
        speeds.add(null);

        // The ending order should always be the same.  This tests for that:
        Collections.shuffle(speeds);
        outData.addAll(speeds);
        return ResultCode.SUCCESS;
    }
}
