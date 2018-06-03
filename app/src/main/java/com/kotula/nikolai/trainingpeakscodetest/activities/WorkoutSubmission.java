package com.kotula.nikolai.trainingpeakscodetest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.kotula.nikolai.trainingpeakscodetest.R;

public class WorkoutSubmission extends AppCompatActivity {
    private static final String TAG = "WorkoutSubmission";

    private EditText mWorkoutTagText;

    public static final String WORKOUT_TAG = "com.kotula.nikolai.trainingpeakscodetest.WORKOUT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_submission);

        // Find the text entry for the workout tag:
        mWorkoutTagText = findViewById(R.id.editText_workoutTag);
    }

    /**
     * Button handler for the "Get Workout" button.
     * @param view The Button view.
     */
    public void getWorkout(View view) {

        // Don't continue if something went wrong with the layout:
        if (mWorkoutTagText != null) {
            Intent workoutExplorerIntent = new Intent(this, WorkoutExplorer.class);

            // Put the workout tag text as the data to pass to the explorer activity:
            // TODO:  Verify null String is handled gracefully
            workoutExplorerIntent.putExtra(WORKOUT_TAG, mWorkoutTagText.getText().toString());
            startActivity(workoutExplorerIntent);
        }
    }
}
