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
import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.models.HeartRateModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PeakHeartRateFragment extends Fragment implements LifecycleOwner {

    private static final String TAG = "PeakHeartRateFragment";

    private LifecycleRegistry mLifecycleRegistry = null;
    private OnListFragmentInteractionListener mListener = null;
    private PeakHeartRateRecyclerViewAdapter mViewAdapter = null;
    private HeartRateModel mHeartRateModel = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeakHeartRateFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PeakHeartRateFragment newInstance(String workoutTag) {
        PeakHeartRateFragment fragment = new PeakHeartRateFragment();
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
        mHeartRateModel = ViewModelProviders.of(this).get(HeartRateModel.class);

        // Always remember to call init() on this after getting an instance from the view model providers!
        mHeartRateModel.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peakheartrate_list, container, false);

        // Set the view and adapter:
        if (view instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) view;

            // Create the observer for the LiveData:
            final Observer<List<PeakHeartRate>> liveDataObserver = new Observer<List<PeakHeartRate>>() {
                @Override
                public void onChanged(@Nullable List<PeakHeartRate> peakHeartRates) {
                    if (mViewAdapter == null) {
                        mViewAdapter = new PeakHeartRateRecyclerViewAdapter(peakHeartRates, mListener);
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
            mHeartRateModel.getPeakHeartRates(workoutTag).observe(this, liveDataObserver);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            Log.d(TAG, "onAttach()");
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
        void onListFragmentInteraction(PeakHeartRate item);
    }
}
