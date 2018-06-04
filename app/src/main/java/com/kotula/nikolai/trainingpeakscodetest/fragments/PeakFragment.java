package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.support.v4.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public abstract class PeakFragment extends Fragment implements LifecycleOwner {
    private static final String TAG = "PeakFragment";

    protected LifecycleRegistry mLifecycleRegistry;
    protected PeakFragment.OnListFragmentInteractionListener mListener;

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
