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

    private IWorkoutReceiver mReciever;

    /**
     * Public constructor
     *
     * @param handler The thread handler to use.
     * @param receiver The object to handle the callback with the data.
     */
    public WorkoutResultReceiver(Handler handler, IWorkoutReceiver receiver) {
        super(handler);
        mReciever = receiver;
    }

    /**
     * Interface for enforcing a concrete implementation to handle data callback.
     */
    public interface IWorkoutReceiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReciever != null) {
            Log.d(TAG, "onReceiveResult()");
            mReciever.onReceiveResult(resultCode, resultData);
        }
    }
}
