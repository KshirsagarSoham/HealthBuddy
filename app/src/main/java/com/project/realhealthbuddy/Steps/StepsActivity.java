package com.project.realhealthbuddy.Steps;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import android.os.Handler;






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

    private TextView tvDistance, tvCalories;





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


        Button btnSetGoal = findViewById(R.id.btnSetGoal);

        btnSetGoal.setOnClickListener(v -> showGoalDialog());

    // Refresh

        ProgressBar progressRefresh = findViewById(R.id.progressRefresh);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(v -> {

            btnRefresh.setVisibility(View.INVISIBLE);
            progressRefresh.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {

                // Get current percentage
                int currentSteps = Integer.parseInt(stepsText.getText().toString());
                int percentage = (int) ((currentSteps * 100.0f) / stepGoal);
                if (percentage > 100) percentage = 100;

                // Reset and re-animate
                progressBar.setProgress(0);
                animateProgress(percentage);

                percentageText.setText(percentage + "%");

                progressRefresh.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.VISIBLE);

            }, 800);
        });

    // Extra metrics distance and calories

        tvDistance = findViewById(R.id.tvDistance);
        tvCalories = findViewById(R.id.tvCalories);




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


        ImageView back = findViewById(R.id.backbutton3);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (stepSensor != null) {
            sensorManager.registerListener(this,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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

                if (totalSteps < todayEntity.baseValue) {
                    // Phone reboot detected
                    todayEntity.baseValue = totalSteps;
                    todaySteps = 0;
                } else {
                    todaySteps = (int) (totalSteps - todayEntity.baseValue);
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

            updateExtraMetrics(todaySteps);


            // Refresh 7-day history list properly
            if (historyAdapter != null) {
                List<StepsEntity> updatedList = repository.getLast7Days();
                historyAdapter.updateList(updatedList);
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

    private void showGoalDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Step Goal");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter goal (e.g. 8000)");
        input.setText(String.valueOf(stepGoal)); // show current goal

        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {

            String value = input.getText().toString().trim();

            if (!value.isEmpty()) {

                stepGoal = Integer.parseInt(value);

                if (stepGoal <= 0) stepGoal = 8000; // safety fallback

                // Save goal
                SharedPreferences prefs =
                        getSharedPreferences("steps_prefs", MODE_PRIVATE);

                prefs.edit().putInt("step_goal", stepGoal).apply();

                goalText.setText("Goal: " + stepGoal + " steps");

                updateProgressUI(); // recalculate immediately
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateProgressUI() {

        int currentSteps = Integer.parseInt(stepsText.getText().toString());

        int percentage = (int) ((currentSteps * 100.0f) / stepGoal);

        if (percentage > 100) percentage = 100;

        animateProgress(percentage);
        percentageText.setText(percentage + "%");
    }

    private void updateExtraMetrics(int steps) {

        // Distance calculation
        float stepLength = 0.75f; // meters
        float distanceKm = (steps * stepLength) / 1000f;

        // Calories calculation
        float calories = steps * 0.04f;

        tvDistance.setText(String.format("Distance: %.2f km", distanceKm));
        tvCalories.setText(String.format("Calories: %.0f kcal", calories));
    }


}
