package com.kotula.nikolai.trainingpeakscodetest.models;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutResultReceiver;
import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutService;

/**
 * {@link ViewModel} Implementation which acts as the model for fetching Workout data.
 */
public class HeartRateModel extends ViewModel implements WorkoutResultReceiver.IWorkoutReceiver,
                                                         LifecycleObserver {
    private static final String TAG = "HeartRateModel";

    private Context mContext;
    private WorkoutResultReceiver mResultReceiver;

    public void init(Context context) {
        mContext = context;
        mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
    }

    public void updatePeakHeartRates(String workoutTag) {
        WorkoutService.startActionFetchPeakHeartRates(mContext, mResultReceiver, workoutTag);
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, "onReceiveResult()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void createReceiver() {
        mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void enableReceiver() {
        if (mResultReceiver != null) {
            mResultReceiver.setReceiver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disableReceiver() {
        if (mResultReceiver != null) {
            mResultReceiver.removeReceiver();
        }
    }
}
