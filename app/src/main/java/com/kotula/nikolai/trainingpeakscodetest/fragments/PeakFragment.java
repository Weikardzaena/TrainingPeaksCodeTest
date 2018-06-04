package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.activities.WorkoutSubmission;

public abstract class PeakFragment extends Fragment implements LifecycleOwner {
    private static final String TAG = "PeakFragment";

    public static final String INTENT_TAG = "com.kotula.nikolai.trainingpeakscodetest.fragments.PeakFragment.INTENT_TAG";
    public static final int INTENT_REFRESH = 100;
    public static final int INTENT_FINISH = 200;

    protected LifecycleRegistry mLifecycleRegistry;
    protected PeakFragment.OnListFragmentInteractionListener mListener;
    protected String mWorkoutTag;

    /**
     * Adds an {@link ErrorDialog} to the foreground and on top of the fragment transaction stack.
     * @param errorMsg The error message to display on the dialog.
     */
    protected void showErrorDialog(String errorMsg) {
        if (getFragmentManager() != null) {

            // Begin transaction:
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // If we already have the same dialog up for some reason, replace it:
            Fragment prev = getFragmentManager().findFragmentByTag(ErrorDialog.ERROR_DIALOG_TAG);
            if (prev != null) {
                transaction.remove(prev);
            }

            // Create the instance:
            ErrorDialog dialog = ErrorDialog.newInstance(errorMsg);
            dialog.setTargetFragment(this, 0);  // the Request code is optional.

            // Show the dialog:
            dialog.show(getFragmentManager(), ErrorDialog.ERROR_DIALOG_TAG);
            transaction.commit();
        }
    }

    /**
     * Mainly here to handle the {@link ErrorDialog} Intents.
     * <p/>
     * It is up to the implementers to decide what to do with the {@link Intent}.
     * @param requestCode The optional request code to sync up request types (not used in this case).
     * @param resultCode The system-provided result of the activity's result which indicates success or failure.
     * @param data The {@link Intent} to execute.
     */
    @Override
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register this fragment to trigger the ON_CREATE Lifecycle events for listeners:
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PeakFragment.OnListFragmentInteractionListener) {
            Log.d(TAG, "onAttach");

            mListener = (PeakFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        if (getArguments() != null) {
            mWorkoutTag = getArguments().getString(WorkoutSubmission.WORKOUT_TAG);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Going from RESUMED to STARTED triggers the ON_PAUSED Lifecycle event.
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        super.onDestroy();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener<T> {
        // TODO: Update argument type and name
        void onListFragmentInteraction(T item);
    }
}
