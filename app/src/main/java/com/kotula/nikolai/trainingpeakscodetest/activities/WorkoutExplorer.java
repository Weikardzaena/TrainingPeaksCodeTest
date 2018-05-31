package com.kotula.nikolai.trainingpeakscodetest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kotula.nikolai.trainingpeakscodetest.R;

public class WorkoutExplorer extends AppCompatActivity {
    private static final String TAG = "WorkoutExplorer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_explorer);

        Intent intent = getIntent();
        String workoutTag = intent.getStringExtra(WorkoutSubmission.WORKOUT_TAG);

        if (workoutTag != null)
            Log.d(TAG, workoutTag);
        else
            Log.d(TAG, "No workout tag!");
    }
}
