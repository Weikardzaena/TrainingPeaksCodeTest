package com.kotula.nikolai.trainingpeakscodetest.models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutResultReceiver;

/**
 * {@link ViewModel} Implementation which acts as the model for fetching Workout data.
 */
public class WorkoutModel extends ViewModel implements WorkoutResultReceiver.IWorkoutReceiver {
    private static final String TAG = "WorkoutModel";

    WorkoutResultReceiver mResultReceiver;

    public WorkoutModel() {
        mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, "onReceiveResult()");
    }
}
