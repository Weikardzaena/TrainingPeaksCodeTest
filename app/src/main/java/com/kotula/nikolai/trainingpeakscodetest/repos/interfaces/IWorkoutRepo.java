package com.kotula.nikolai.trainingpeakscodetest.repos.interfaces;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;

import java.util.List;

/**
 * Interface for abstracting the Workout data sources away from the concrete implementation.
 */
public interface IWorkoutRepo {
    /**
     * Fetches a {@link List} of {@link PeakHeartRate} objects from the data source.
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @return The {@link List} of {@link PeakHeartRate} objects from the data source.
     */
    public List<PeakHeartRate> getPeakHeartRates(String workoutTag);

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @return The {@link List} of {@link PeakSpeed} objects from the data source.
     */
    public List<PeakSpeed> getPeakSpeeds(String workoutTag);
}
