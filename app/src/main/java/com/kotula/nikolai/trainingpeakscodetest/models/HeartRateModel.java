package com.kotula.nikolai.trainingpeakscodetest.models;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRateComparator;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutResultReceiver;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * {@link ViewModel} Implementation which acts as the model for fetching Workout data.
 */
public class HeartRateModel extends PeakModel<PeakHeartRate> implements WorkoutResultReceiver.IWorkoutReceiver,
                                                                        LifecycleObserver {
    private static final String TAG = "HeartRateModel";

    /**
     * Callback for when the ResultReceiver has finished its work and has results.
     * @param resultCode The status code.
     * @param resultData The {@link Bundle} that contains the data.
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, "onReceiveResult()");
        ArrayList<PeakHeartRate> heartRates = resultData.getParcelableArrayList(PeakHeartRate.PARCEL_PEAK_HEART_RATE);
        if (heartRates != null) {
            // Remove duplicates by adding everything to a Set:
            ArrayList<PeakHeartRate> trimmedVals = new ArrayList<>(new HashSet<>(heartRates));

            // If a null value remains, remove it.
            trimmedVals.remove(null);

            // Always sort AFTER the Hash Set because of ordering and for slight performance gains.
            Collections.sort(trimmedVals, new PeakHeartRateComparator());

            mData.setValue(trimmedVals);
        }
    }

    /**
     * Public interface for fetching peak heart rate data and updating the {@link ViewModel} {@link LiveData}.
     * @param workoutTag The Workout Tag to fetch from the data source.
     */
    @Override
    public LiveData<List<PeakHeartRate>> getData(String workoutTag) {
        WorkoutService.startActionFetchPeakHeartRates(mContext, mResultReceiver, workoutTag);
        return mData;
    }
}
