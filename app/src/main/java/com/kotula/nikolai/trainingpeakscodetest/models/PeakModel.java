package com.kotula.nikolai.trainingpeakscodetest.models;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.kotula.nikolai.trainingpeakscodetest.services.WorkoutResultReceiver;

import java.util.List;

public abstract class PeakModel<T> extends ViewModel implements WorkoutResultReceiver.IWorkoutReceiver,
                                                                LifecycleObserver {

    protected Context mContext; // So long as we always remember to null this ON_DESTROY lifecycle event, holding a reference to the Context is fine.
    protected WorkoutResultReceiver mResultReceiver;
    protected MutableLiveData<List<T>> mData = new MutableLiveData<>();

    /**
     * Callback for when the ResultReceiver has finished its work and has results.
     * @param resultCode The status code.
     * @param resultData The {@link Bundle} that contains the data.
     */
    public abstract void onReceiveResult(int resultCode, Bundle resultData);

    /**
     * Public interface for fetching peak heart rate data and updating the {@link ViewModel} {@link LiveData}.
     * @param workoutTag The Workout Tag to fetch from the data source.
     */
    public abstract LiveData<List<T>> getData(String workoutTag);

    /**
     * Initializes parameters for this {@link ViewModel}.  This MUST be called after getting an instance of this from {@link android.arch.lifecycle.ViewModelProviders}.
     * @param context The application context.
     */
    public void init(Context context) {
        mContext = context;
        mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
    }

    /**
     * Binds to the ON_CREATE Lifecycle event for activities/fragments to instantiate the Result Receiver.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void createReceiver() {
        if (mResultReceiver == null) {
            mResultReceiver = new WorkoutResultReceiver(new Handler(), this);
        }
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void removeContext() {
        mContext = null;
    }
}
