package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * A fragment representing a list of Peak Heart Rate entries.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PeakHeartRateFragment extends PeakFragment {

    private static final String TAG = "PeakHeartRateFragment";

    private PeakHeartRateRecyclerViewAdapter mViewAdapter = null;
    private HeartRateModel mHeartRateModel = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeakHeartRateFragment() {
    }

    @SuppressWarnings("unused")
    public static PeakHeartRateFragment newInstance(String workoutTag) {
        PeakHeartRateFragment fragment = new PeakHeartRateFragment();
        Bundle args = new Bundle();
        args.putString(WorkoutSubmission.WORKOUT_TAG, workoutTag);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                        // For now, the entire data set is being updated at once, so we notify the
                        // view adapter to refresh everything.  This should be changed to something
                        // like notifyItemChanged(index) if individual items are changing.
                        mViewAdapter.notifyDataSetChanged();
                    }
                }
            };

            // Wire up the Model observer with the provided parameters:
            String workoutTag = null;
            if (getArguments() != null) {
                workoutTag = getArguments().getString(WorkoutSubmission.WORKOUT_TAG);
            }
            mHeartRateModel.getData(workoutTag).observe(this, liveDataObserver);
        }
        return view;
    }
}
