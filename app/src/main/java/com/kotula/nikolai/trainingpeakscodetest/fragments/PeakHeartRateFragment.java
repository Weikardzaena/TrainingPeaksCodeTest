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
import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.models.HeartRateModel;
import com.kotula.nikolai.trainingpeakscodetest.models.ModelStatus;

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
            mHeartRateModel.getStatus().observe(this, modelStatusObserver);
            mHeartRateModel.getData(mWorkoutTag).observe(this, liveDataObserver);
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
                        mHeartRateModel.getData(mWorkoutTag);
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
