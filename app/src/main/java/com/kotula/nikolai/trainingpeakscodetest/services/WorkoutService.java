package com.kotula.nikolai.trainingpeakscodetest.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.kotula.nikolai.trainingpeakscodetest.repos.concrete.HttpWorkoutRESTClient;
import com.kotula.nikolai.trainingpeakscodetest.repos.concrete.UnitTestWorkoutRepo;
import com.kotula.nikolai.trainingpeakscodetest.repos.interfaces.IWorkoutRepo;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WorkoutService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.kotula.nikolai.trainingpeakscodetest.services.action.FOO";
    private static final String ACTION_BAZ = "com.kotula.nikolai.trainingpeakscodetest.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.kotula.nikolai.trainingpeakscodetest.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.kotula.nikolai.trainingpeakscodetest.services.extra.PARAM2";

    private IWorkoutRepo mWorkoutRepo;

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
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WorkoutService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WorkoutService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
