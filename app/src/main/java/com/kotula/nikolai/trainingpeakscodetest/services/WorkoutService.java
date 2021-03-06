package com.kotula.nikolai.trainingpeakscodetest.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.repos.ResultCode;
import com.kotula.nikolai.trainingpeakscodetest.repos.concrete.HttpWorkoutRESTClient;
import com.kotula.nikolai.trainingpeakscodetest.repos.concrete.UnitTestWorkoutRepo;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class WorkoutService extends IntentService {

    private static final String TAG = "WorkoutService";
    private static final String ACTION_FETCH_PEAK_HEART_RATES = "com.kotula.nikolai.trainingpeakscodetest.services.action.FETCH_PEAK_HEART_RATES";
    private static final String ACTION_FETCH_PEAK_SPEEDS = "com.kotula.nikolai.trainingpeakscodetest.services.action.FETCH_PEAK_SPEEDS";

    // These aren't an Enum because we need integers for the result codes:
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_CONNECTION_FAILURE = -100;
    public static final int RESULT_PARSE_FAILURE = -200;
    public static final int RESULT_UNKNOWN_FAILURE = -1000;

    private static final String EXTRA_WORKOUT_TAG = "com.kotula.nikolai.trainingpeakscodetest.services.extra.WORKOUT_TAG";

    private IWorkoutRepo mWorkoutRepo;
    private ResultReceiver mResultReceiver;

    public WorkoutService() {
        super("WorkoutService");

        // I REALLY wanted to use Dependency Injection here, but not having used DI in Android
        // before, and with time constraints, I would rather just manually swap out the concrete
        // classes here than spend the time to learn an entire library like Dagger.
        //
        // LET IT BE SAID that Dependency Injection is vastly superior to what I'm doing here, but
        // for a simple one-off code test, this is fine.
        mWorkoutRepo = new HttpWorkoutRESTClient();
        //mWorkoutRepo = new UnitTestWorkoutRepo();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFetchPeakHeartRates(Context context, WorkoutResultReceiver receiver, String workoutTag) {
        Intent intent = new Intent(context, WorkoutService.class);
        intent.setAction(ACTION_FETCH_PEAK_HEART_RATES);
        intent.putExtra(EXTRA_WORKOUT_TAG, workoutTag);
        intent.putExtra(WorkoutResultReceiver.WORKOUT_RESULTS_RECEIVER_TAG, receiver);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFetchPeakSpeeds(Context context, WorkoutResultReceiver receiver, String workoutTag) {
        Intent intent = new Intent(context, WorkoutService.class);
        intent.setAction(ACTION_FETCH_PEAK_SPEEDS);
        intent.putExtra(EXTRA_WORKOUT_TAG, workoutTag);
        intent.putExtra(WorkoutResultReceiver.WORKOUT_RESULTS_RECEIVER_TAG, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mResultReceiver = intent.getParcelableExtra(WorkoutResultReceiver.WORKOUT_RESULTS_RECEIVER_TAG);
            if ((mResultReceiver != null) && (mWorkoutRepo != null)) {
                final String action = intent.getAction();
                if (ACTION_FETCH_PEAK_HEART_RATES.equals(action)) {
                    final String workoutTag = intent.getStringExtra(EXTRA_WORKOUT_TAG);
                    handleActionFetchPeakHeartRates(workoutTag);
                } else if (ACTION_FETCH_PEAK_SPEEDS.equals(action)) {
                    final String workoutTag = intent.getStringExtra(EXTRA_WORKOUT_TAG);
                    handleActionFetchPeakSpeeds(workoutTag);
                }
            }
        }
    }

    /**
     * Handle action Fetch_Peak_Heart_Rates in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetchPeakHeartRates(String workoutTag) {
        if ((mWorkoutRepo != null) && (mResultReceiver != null)) {
            Bundle bundle = new Bundle();
            int statusCode = RESULT_UNKNOWN_FAILURE;

            ArrayList<PeakHeartRate> peakHeartRates = new ArrayList<>();
            ResultCode resultCode = mWorkoutRepo.getPeakHeartRates(workoutTag, peakHeartRates);

            // Build the Service result codes from the Repo result codes:
            switch (resultCode) {
                case FAIL_CONNECTION:
                    statusCode = RESULT_CONNECTION_FAILURE;
                    break;
                case FAIL_PARSE:
                    statusCode = RESULT_PARSE_FAILURE;
                    break;
                case FAIL_STREAM:
                    statusCode = RESULT_CONNECTION_FAILURE;
                    break;
                case SUCCESS:
                    statusCode = RESULT_SUCCESS;
                    break;
                default:
                    break;
            }

            bundle.putParcelableArrayList(PeakHeartRate.PARCEL_PEAK_HEART_RATE, new ArrayList<Parcelable>(peakHeartRates));

            mResultReceiver.send(statusCode, bundle);
        }
    }

    /**
     * Handle action Fetch_Peak_Speeds in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetchPeakSpeeds(String workoutTag) {
        if ((mWorkoutRepo != null) && (mResultReceiver != null)) {
            Bundle bundle = new Bundle();
            ArrayList<PeakSpeed> peakSpeeds = new ArrayList<>();
            mWorkoutRepo.getPeakSpeeds(workoutTag, peakSpeeds);

            if (peakSpeeds != null) {
                bundle.putParcelableArrayList(PeakSpeed.PARCEL_PEAK_SPEED, new ArrayList<Parcelable>(peakSpeeds));
            } else {
                Log.e(TAG, "Something went wrong with fetching the data!");
            }

            mResultReceiver.send(0, bundle);
        }
    }
}
