package com.kotula.nikolai.trainingpeakscodetest.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import com.kotula.nikolai.trainingpeakscodetest.repos.concrete.HttpWorkoutRESTClient;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class WorkoutService extends IntentService {

    private static final String ACTION_FETCH_PEAK_HEART_RATES = "com.kotula.nikolai.trainingpeakscodetest.services.action.FETCH_PEAK_HEART_RATES";
    private static final String ACTION_FETCH_PEAK_SPEEDS = "com.kotula.nikolai.trainingpeakscodetest.services.action.FETCH_PEAK_SPEEDS";

    private static final String EXTRA_WORKOUT_TAG = "com.kotula.nikolai.trainingpeakscodetest.services.extra.WORKOUT_TAG";

    private IWorkoutRepo mWorkoutRepo;
    private WorkoutResultReceiver mWorkoutReceiver;

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
        intent.setAction(ACTION_FETCH_PEAK_HEART_RATES);
        intent.putExtra(EXTRA_WORKOUT_TAG, workoutTag);
        intent.putExtra(WorkoutResultReceiver.WORKOUT_RESULTS_RECEIVER_TAG, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mWorkoutReceiver = intent.getParcelableExtra(WorkoutResultReceiver.WORKOUT_RESULTS_RECEIVER_TAG);
            if ((mWorkoutReceiver != null) && (mWorkoutRepo != null)) {
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
        if ((mWorkoutRepo != null) && (mWorkoutReceiver != null)) {
            Bundle bundle = new Bundle();
            mWorkoutRepo.getPeakHeartRates(); // blocking operation.
            mWorkoutReceiver.onReceiveResult(0, bundle);
        }
    }

    /**
     * Handle action Fetch_Peak_Speeds in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFetchPeakSpeeds(String workoutTag) {
        if ((mWorkoutRepo != null) && (mWorkoutReceiver != null)) {
            Bundle bundle = new Bundle();
            mWorkoutRepo.getPeakSpeeds(); // blocking operation.
            mWorkoutReceiver.onReceiveResult(0, bundle);
        }
    }
}
