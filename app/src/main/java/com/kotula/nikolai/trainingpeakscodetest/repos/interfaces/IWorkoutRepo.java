package com.kotula.nikolai.trainingpeakscodetest.repos.interfaces;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.ResultCode;

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
     * @param outData The {@link List} of {@link PeakHeartRate} objects that will be filled with the fetched data.
     * @return A code indicating the result of the operation.
     */
    public ResultCode getPeakHeartRates(String workoutTag, List<PeakHeartRate> outData);

    /**
     * Fetches a {@link List} of {@link PeakSpeed} objects from the data source.
     * <p/>
     * This is a BLOCKING operation.
     * @param workoutTag The Workout Tag to fetch from the endpoint.
     * @param outData The {@link List} of {@link PeakSpeed} objects that will be filled with the fetched data.
     * @return A code indicating the result of the operation.
     */
    public ResultCode getPeakSpeeds(String workoutTag, List<PeakSpeed> outData);
}
