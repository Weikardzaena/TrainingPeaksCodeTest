package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.kotula.nikolai.trainingpeakscodetest.models.ModelStatus;
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

            // React to changes in the Model's status:
            final Observer<ModelStatus> modelStatusObserver = new Observer<ModelStatus>() {
                @Override
                public void onChanged(@Nullable ModelStatus modelStatus) {
                    if (modelStatus != null) {
                        switch (modelStatus) {
                            case FETCHING:
                                // TODO:  Show a spinner or something.
                                break;
                            case FINISHED_ERROR_CONNECTION:
                                showErrorDialog(getString(R.string.lbl_error_connection));
                                break;
                            case FINISHED_ERROR_DATA_FORMAT:
                                showErrorDialog(getString(R.string.lbl_error_parse));
                                break;
                            case FINISHED_ERROR_UNKNOWN:
                                showErrorDialog(getString(R.string.lbl_error_unknown));
                                break;
                            default:
                                // No error dialog because success.
                                break;
                        }
                    }
                }
            };

            // Wire up the Model observer with the provided parameters:
            mSpeedModel.getStatus().observe(this, modelStatusObserver);
            mSpeedModel.getData(mWorkoutTag).observe(this, liveDataObserver);
        }
        return view;
    }

    /**
     * Mainly here to handle the {@link ErrorDialog} Intents.
     * @param requestCode The optional request code to sync up request types (not used in this case).
     * @param resultCode The system-provided result of the activity's result which indicates success or failure.
     * @param data The {@link Intent} to execute.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) { // Not sure if I actually need to check this if the result is OK.

                int intentCode = data.getIntExtra(PeakFragment.INTENT_TAG, 0);

                switch (intentCode) {
                    case PeakFragment.INTENT_REFRESH:
                        // Refresh the data:
                        mSpeedModel.getData(mWorkoutTag);
                        break;

                    case PeakFragment.INTENT_FINISH:
                        // The user clicked "Go Back," so we end the activity:
                        if (getActivity() != null)
                            getActivity().finish();
                        break;

                    default:
                        // Unknown intent code.
                        break;
                }
            }
        }
    }
}
