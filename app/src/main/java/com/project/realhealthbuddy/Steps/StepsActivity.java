package com.project.realhealthbuddy.Steps;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.Steps.Repository.StepsRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StepsActivity extends AppCompatActivity {

    private TextView stepsText;
    private StepsRepository repository;
    private StepsEntity todayEntity;
    private String todayDate;

    private StepsHistoryAdapter historyAdapter;

    private int stepGoal = 8000;
    private ProgressBar progressBar;
    private TextView goalText, percentageText;

    private TextView tvDistance, tvCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        stepsText = findViewById(R.id.tvTodaySteps);
        repository = new StepsRepository(this);

        // ✅ Start Foreground Service
        Intent intent = new Intent(this, StepService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        // Date
        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Load today data
        todayEntity = repository.getStepsByDate(todayDate);

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.stepsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<StepsEntity> last7Days = repository.getLast7Days();
        historyAdapter = new StepsHistoryAdapter(last7Days, todayDate);
        recyclerView.setAdapter(historyAdapter);

        // Goal UI
        goalText = findViewById(R.id.tvGoal);
        percentageText = findViewById(R.id.tvPercentage);
        progressBar = findViewById(R.id.progressBar);

        SharedPreferences prefs = getSharedPreferences("steps_prefs", MODE_PRIVATE);
        stepGoal = prefs.getInt("step_goal", 8000);

        goalText.setText("Goal: " + stepGoal + " steps");

        Button btnSetGoal = findViewById(R.id.btnSetGoal);
        btnSetGoal.setOnClickListener(v -> showGoalDialog());

        // Refresh Button
        ProgressBar progressRefresh = findViewById(R.id.progressRefresh);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(v -> {

            btnRefresh.setVisibility(View.INVISIBLE);
            progressRefresh.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {

                loadTodaySteps(); // 🔥 important

                progressRefresh.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.VISIBLE);

            }, 800);
        });

        // Extra metrics
        tvDistance = findViewById(R.id.tvDistance);
        tvCalories = findViewById(R.id.tvCalories);

        // Back button
        ImageView back = findViewById(R.id.backbutton3);
        back.setOnClickListener(v -> finish());

        // 🔥 Load initial data
        loadTodaySteps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodaySteps(); // refresh UI when returning
    }

    // ✅ CENTRAL METHOD (VERY IMPORTANT)
    private void loadTodaySteps() {

        todayEntity = repository.getStepsByDate(todayDate);

        int todaySteps = (todayEntity != null) ? todayEntity.steps : 0;

        stepsText.setText(String.valueOf(todaySteps));

        int percentage = (int) ((todaySteps * 100.0f) / stepGoal);
        if (percentage > 100) percentage = 100;

        progressBar.setProgress(0);
        animateProgress(percentage);
        percentageText.setText(percentage + "%");

        updateExtraMetrics(todaySteps);

        // Update RecyclerView
        if (historyAdapter != null) {
            List<StepsEntity> updatedList = repository.getLast7Days();
            historyAdapter.updateList(updatedList);
        }
    }

    private void animateProgress(int targetProgress) {

        android.animation.ObjectAnimator animation =
                android.animation.ObjectAnimator.ofInt(progressBar, "progress",
                        progressBar.getProgress(), targetProgress);

        animation.setDuration(800);
        animation.start();
    }

    private void showGoalDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Step Goal");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter goal (e.g. 8000)");
        input.setText(String.valueOf(stepGoal));

        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {

            String value = input.getText().toString().trim();

            if (!value.isEmpty()) {

                stepGoal = Integer.parseInt(value);
                if (stepGoal <= 0) stepGoal = 8000;

                SharedPreferences prefs =
                        getSharedPreferences("steps_prefs", MODE_PRIVATE);

                prefs.edit().putInt("step_goal", stepGoal).apply();

                goalText.setText("Goal: " + stepGoal + " steps");

                loadTodaySteps(); // refresh UI
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateExtraMetrics(int steps) {

        float stepLength = 0.75f;
        float distanceKm = (steps * stepLength) / 1000f;
        float calories = steps * 0.04f;

        tvDistance.setText(String.format("Distance: %.2f km", distanceKm));
        tvCalories.setText(String.format("Calories: %.0f kcal", calories));
    }
}