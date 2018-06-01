package com.kotula.nikolai.trainingpeakscodetest.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * A simple wrapper around the {@link ResultReceiver} class to propagate result data to the model.
 */
public class WorkoutResultReceiver extends ResultReceiver {

    private static final String TAG = "WorkoutResultsReceiver";

    public static final String WORKOUT_RESULTS_RECEIVER_TAG = "com.kotula.nikolai.trainingpeakscodetest.services.tag.WORKOUT_RESULTS_RECEIVER";

    private IWorkoutReceiver mReceiver;

    /**
     * Public constructor
     *
     * @param handler The thread handler to use.
     * @param receiver The object to handle the callback with the data.
     */
    public WorkoutResultReceiver(Handler handler, IWorkoutReceiver receiver) {
        super(handler);
        mReceiver = receiver;
    }

    /**
     * Interface for enforcing a concrete implementation to handle data callback.
     */
    public interface IWorkoutReceiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void removeReceiver() {
        mReceiver = null;
    }

    public void setReceiver(IWorkoutReceiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            Log.d(TAG, "onReceiveResult()");
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
