package com.kotula.nikolai.trainingpeakscodetest.repos.concrete;

import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

import java.util.ArrayList;
import java.util.List;

public class UnitTestWorkoutRepo implements IWorkoutRepo {
    private static final String TAG = "UnitTestWorkoutRepo";

    /**
     * Fetches a {@link List} of {@link PeakHeartRate} objects from the data source.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @return The {@link List} of {@link PeakHeartRate} objects from the data source.
     */
    @Override
    public List<PeakHeartRate> getPeakHeartRates(String workoutTag) {
        Log.d(TAG, "getPeakSpeeds()");

        ArrayList<PeakHeartRate> heartRates = new ArrayList<PeakHeartRate>();
        heartRates.add(new PeakHeartRate(10, 0, 0, 0));
        return heartRates;
    }

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @return The {@link List} of {@link PeakSpeed} objects from the data source.
     */
    @Override
    public List<PeakSpeed> getPeakSpeeds(String workoutTag) {
        Log.d(TAG, "getPeakSpeeds()");
        ArrayList<PeakSpeed> speeds = new ArrayList<PeakSpeed>();
        speeds.add(new PeakSpeed(1, 0, 0, 0));
        return speeds;
    }
}
