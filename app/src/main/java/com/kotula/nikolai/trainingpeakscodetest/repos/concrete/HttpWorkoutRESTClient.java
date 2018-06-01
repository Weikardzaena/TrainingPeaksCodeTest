package com.kotula.nikolai.trainingpeakscodetest.repos.concrete;

import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the data source for Workout data.
 */
public class HttpWorkoutRESTClient implements IWorkoutRepo {
    private static final String TAG = "HttpWorkoutRESTClient";

    /**
     * Fetches a {@link List} of {@link PeakHeartRate} objects from the data source.
     * @return The {@link List} of {@link PeakHeartRate} objects from the data source.
     */
    @Override
    public List<PeakHeartRate> getPeakHeartRates() {
        Log.d(TAG, "getPeakHeartRates()");
        return new ArrayList<PeakHeartRate>();
    }

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * @return The {@link List} of {@link PeakSpeed} objects from the data source.
     */
    @Override
    public List<PeakSpeed> getPeakSpeeds() {
        Log.d(TAG, "getPeakSpeeds()");
        return new ArrayList<PeakSpeed>();
    }
}
