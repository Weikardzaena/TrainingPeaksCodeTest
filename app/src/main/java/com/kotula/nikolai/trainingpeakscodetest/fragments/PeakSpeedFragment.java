package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kotula.nikolai.trainingpeakscodetest.R;
import com.kotula.nikolai.trainingpeakscodetest.activities.WorkoutSubmission;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.models.SpeedModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PeakSpeedFragment extends Fragment implements LifecycleOwner {

    private static final String TAG = "PeakSpeedFragment";

    private LifecycleRegistry mLifecycleRegistry;
    private OnListFragmentInteractionListener mListener;
    private PeakSpeedRecyclerViewAdapter mViewAdapter = null;
    private SpeedModel mSpeedModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeakSpeedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PeakSpeedFragment newInstance(String workoutTag) {
        PeakSpeedFragment fragment = new PeakSpeedFragment();
        Bundle args = new Bundle();
        args.putString(WorkoutSubmission.WORKOUT_TAG, workoutTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register this fragment to trigger the ON_CREATE Lifecycle events for listeners:
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the ViewModel for this fragment.
        mSpeedModel = ViewModelProviders.of(this).get(SpeedModel.class);

        // Always remember to call init() on this after getting an instance from the view model providers!
        mSpeedModel.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peakspeed_list, container, false);

        // Set the view and adapter:
        if (view instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) view;

            // Create the observer for the LiveData:
            final Observer<List<PeakSpeed>> liveDataObserver = new Observer<List<PeakSpeed>>() {
                @Override
                public void onChanged(@Nullable List<PeakSpeed> peakSpeeds) {
                    if (mViewAdapter == null) {
                        mViewAdapter = new PeakSpeedRecyclerViewAdapter(peakSpeeds, mListener);
                        recyclerView.setAdapter(mViewAdapter);
                    } else {
                        mViewAdapter.notifyDataSetChanged();
                    }
                }
            };

            // Wire up the Model observer with the provided parameters:
            String workoutTag = null;
            if (getArguments() != null) {
                workoutTag = getArguments().getString(WorkoutSubmission.WORKOUT_TAG);
            }
            mSpeedModel.getData(workoutTag).observe(this, liveDataObserver);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            Log.d(TAG, "onAttach");

            mListener = (OnListFragmentInteractionListener) context;
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(PeakSpeed item);
    }
}
