package com.project.realhealthbuddy.Steps;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.Steps.Repository.StepsRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.project.realhealthbuddy.Steps.StepsHistoryAdapter;
import java.util.List;

import android.content.SharedPreferences;
import android.widget.ProgressBar;

import android.animation.ObjectAnimator;





public class StepsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private boolean isSensorPresent = false;

    private float totalSteps = 0f;

    private TextView stepsText;

    private StepsRepository repository;
    private StepsEntity todayEntity;

    private String todayDate;

    private StepsHistoryAdapter historyAdapter;
    private int stepGoal = 8000; // default goal
    private ProgressBar progressBar;
    private TextView goalText;
    private TextView percentageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);



        stepsText = findViewById(R.id.tvTodaySteps);

        repository = new StepsRepository(this);

        // Runtime permission for Android 10+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION},
                    100
            );
        }

        // Get today's date
        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Load today's record
        todayEntity = repository.getStepsByDate(todayDate);

        RecyclerView recyclerView = findViewById(R.id.stepsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        List<StepsEntity> last7Days = repository.getLast7Days();
//
//        StepsHistoryAdapter adapter = new StepsHistoryAdapter(last7Days);
//        recyclerView.setAdapter(adapter);

        List<StepsEntity> last7Days = repository.getLast7Days();
        historyAdapter = new StepsHistoryAdapter(last7Days,todayDate);
        recyclerView.setAdapter(historyAdapter);


        // Goal ============================

        goalText = findViewById(R.id.tvGoal);
        percentageText = findViewById(R.id.tvPercentage);
        progressBar = findViewById(R.id.progressBar);

        // Load goal from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("steps_prefs", MODE_PRIVATE);
        stepGoal = prefs.getInt("step_goal", 8000);

        goalText.setText("Goal: " + stepGoal + " steps");



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        if (stepSensor != null) {
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
            Toast.makeText(this, "Step Sensor Not Available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isSensorPresent) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            totalSteps = event.values[0];

            int todaySteps = 0;

            // First time today → save baseline
            if (todayEntity == null) {

                todayEntity = new StepsEntity(todayDate, totalSteps, 0);
                repository.insert(todayEntity);

                todaySteps = 0;

            } else {

                todaySteps = (int) (totalSteps - todayEntity.baseValue);

                // Safety check if phone rebooted
                if (todaySteps < 0) {
                    todaySteps = 0;
                }

                todayEntity.steps = todaySteps;
                repository.update(todayEntity);
            }

            // -------- UI UPDATE (common section) --------

            stepsText.setText(String.valueOf(todaySteps));

            // Calculate goal percentage
            int percentage = (int) ((todaySteps * 100.0f) / stepGoal);

            if (percentage > 100) {
                percentage = 100;
            }

            animateProgress(percentage);

            percentageText.setText(percentage + "%");

            // Refresh 7-day history list
            if (historyAdapter != null) {
                historyAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void animateProgress(int targetProgress) {

        ObjectAnimator animation =
                ObjectAnimator.ofInt(progressBar, "progress",
                        progressBar.getProgress(), targetProgress);

        animation.setDuration(800); // 0.8 seconds
        animation.start();
    }

}
