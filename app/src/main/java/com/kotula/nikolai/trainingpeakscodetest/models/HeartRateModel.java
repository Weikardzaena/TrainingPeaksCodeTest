package com.kotula.nikolai.trainingpeakscodetest.models;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutResultReceiver;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutService;

import java.util.ArrayList;

/**
 * {@link ViewModel} Implementation which acts as the model for fetching Workout data.
 */
public class HeartRateModel extends ViewModel implements WorkoutResultReceiver.IWorkoutReceiver,
                                                         LifecycleObserver {
    private static final String TAG = "HeartRateModel";

    private Context mContext;
    private WorkoutResultReceiver mResultReceiver;

    /**
     * Initializes parameters for this {@link ViewModel}.  This MUST be called after getting an instance of this from {@link android.arch.lifecycle.ViewModelProviders}.
     * @param context The application context.
     */
    public void init(Context context) {
        mContext = context;
        mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
    }

    /**
     * Public interface for fetching peak heart rate data and updating the {@link ViewModel} {@link LiveData}.
     * @param workoutTag The Workout Tag to fetch from the data source.
     */
    public void updatePeakHeartRates(String workoutTag) {
        WorkoutService.startActionFetchPeakHeartRates(mContext, mResultReceiver, workoutTag);
    }

    /**
     * Callback for when the ResultReceiver has finished its work and has results.
     * @param resultCode The status code.
     * @param resultData The {@link Bundle} that contains the data.
     */
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, "onReceiveResult()");
        ArrayList<PeakHeartRate> heartRates = resultData.getParcelableArrayList(PeakHeartRate.PARCEL_PEAK_HEART_RATE);
        if (heartRates != null) {
            for (PeakHeartRate heartRate : heartRates) {
                if (heartRate != null) {
                    Log.d(TAG, heartRate.toString());
                }
            }
        }
    }

    /**
     * Binds to the ON_CREATE Lifecycle event for activities/fragments to instantiate the Result Receiver.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void createReceiver() {
        mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
    }


    /**
     * Binds to the ON_RESUME Lifecycle event for activities/fragments to update the Result Receiver with our instance.
     * This is necessary because the reference to this will be removed from the Result Receiver ON_PAUSE.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void enableReceiver() {
        if (mResultReceiver != null) {
            mResultReceiver.setReceiver(this);
        }
    }

    /**
     * Binds to the ON_PAUSE Lifecycle event for activities/fragments to remove the reference to this from the Result Receiver.
     * This is necessary because if the reference to this is left hanging, when the activity/fragment is destroyed, the references could leak.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disableReceiver() {
        if (mResultReceiver != null) {
            mResultReceiver.removeReceiver();
        }
    }
}
