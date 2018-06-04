package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kotula.nikolai.trainingpeakscodetest.R;
import com.kotula.nikolai.trainingpeakscodetest.activities.WorkoutSubmission;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.models.SpeedModel;

import java.util.List;

/**
 * A fragment representing a list of Peak Speed entries.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PeakSpeedFragment extends PeakFragment {

    private static final String TAG = "PeakSpeedFragment";

    private PeakSpeedRecyclerViewAdapter mViewAdapter = null;
    private SpeedModel mSpeedModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeakSpeedFragment() {
    }

    @SuppressWarnings("unused")
    public static PeakSpeedFragment newInstance(String workoutTag) {
        PeakSpeedFragment fragment = new PeakSpeedFragment();
        Bundle args = new Bundle();
        args.putString(WorkoutSubmission.WORKOUT_TAG, workoutTag);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            mSpeedModel.getData(workoutTag).observe(this, liveDataObserver);
        }
        return view;
    }
}
