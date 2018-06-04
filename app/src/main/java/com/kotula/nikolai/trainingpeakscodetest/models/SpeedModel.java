package com.kotula.nikolai.trainingpeakscodetest.models;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeedComparator;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutResultReceiver;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * {@link ViewModel} Implementation which acts as the model for fetching Workout data.
 */
public class SpeedModel extends PeakModel<PeakSpeed> implements WorkoutResultReceiver.IWorkoutReceiver,
                                                                LifecycleObserver {
    private static final String TAG = "SpeedModel";

    /**
     * Callback for when the ResultReceiver has finished its work and has results.
     * @param resultCode The status code.
     * @param resultData The {@link Bundle} that contains the data.
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ArrayList<PeakSpeed> peakSpeeds = resultData.getParcelableArrayList(PeakSpeed.PARCEL_PEAK_SPEED);

        switch (resultCode) {
            case WorkoutService.RESULT_SUCCESS:
                mStatus.setValue(ModelStatus.FINISHED_SUCCESS);
                break;
            case WorkoutService.RESULT_CONNECTION_FAILURE:
                mStatus.setValue(ModelStatus.FINISHED_ERROR_CONNECTION);
                break;
            case WorkoutService.RESULT_PARSE_FAILURE:
                mStatus.setValue(ModelStatus.FINISHED_ERROR_DATA_FORMAT);
                break;
            case WorkoutService.RESULT_UNKNOWN_FAILURE:
                mStatus.setValue(ModelStatus.FINISHED_ERROR_UNKNOWN);
                break;
            default:
                mStatus.setValue(ModelStatus.FINISHED_SUCCESS);
                break;
        }

        // The following set of logic to remove duplicates and sort the array is not optimized, but
        // considering that this is at the tail end of a comparatively very time-consuming operation
        // of fetching and parsing the data from a REST endpoint, the performance gains of
        // optimizing this logic is not worth the time.
        if (peakSpeeds != null) {
            // Remove duplicates by adding everything to a Set:
            ArrayList<PeakSpeed> trimmedSpeeds = new ArrayList<>(new HashSet<>(peakSpeeds));

            // If a null value remains, remove it.
            trimmedSpeeds.remove(null);

            // Always sort AFTER the Hash Set because of ordering.
            Collections.sort(trimmedSpeeds, new PeakSpeedComparator());

            mData.setValue(trimmedSpeeds);
        }
    }

    /**
     * Public interface for fetching peak heart rate data and updating the {@link ViewModel} {@link LiveData}.
     * @param workoutTag The Workout Tag to fetch from the data source.
     */
    @Override
    public LiveData<List<PeakSpeed>> getData(String workoutTag) {
        mStatus.setValue(ModelStatus.FETCHING);
        WorkoutService.startActionFetchPeakSpeeds(mContext, mResultReceiver, workoutTag);
        return mData;
    }
}
