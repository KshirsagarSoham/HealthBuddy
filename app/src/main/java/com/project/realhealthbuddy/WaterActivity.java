package com.project.realhealthbuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WaterActivity extends AppCompatActivity {

    private CircularProgressIndicator waterProgress;
    private TextView tvWaterAmount, tvHydrationPercent;
    private MaterialButton btn250, btn500, btn750;
    private ImageView backbutton;

    private int currentIntake = 0;       // Current water intake in ml
    private final int dailyGoal = 2500;  // Daily goal in ml

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        prefs = getSharedPreferences("health_data", MODE_PRIVATE);

        initViews();
        handleDailyReset();  // Check if new day, reset if needed
        updateUI();

        // Button click listeners
        btn250.setOnClickListener(v -> addWater(250));
        btn500.setOnClickListener(v -> addWater(500));
        btn750.setOnClickListener(v -> addWater(750));

        // Back button
        backbutton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        waterProgress = findViewById(R.id.waterProgress);
        tvWaterAmount = findViewById(R.id.tvWaterAmount);
        tvHydrationPercent = findViewById(R.id.tvHydrationPercent);

        btn250 = findViewById(R.id.btn250);
        btn500 = findViewById(R.id.btn500);
        btn750 = findViewById(R.id.btn750);

        backbutton = findViewById(R.id.backbutton);

        // Set the max value for the progress bar
        waterProgress.setMax(dailyGoal);
    }

    private void handleDailyReset() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = prefs.getString("water_date", "");

        if (!today.equals(lastDate)) {
            // New day → reset water intake
            currentIntake = 0;
            prefs.edit()
                    .putInt("water_ml", 0)
                    .putString("water_date", today)
                    .apply();
        } else {
            // Same day → load saved value
            currentIntake = prefs.getInt("water_ml", 0);
        }
    }

    private void addWater(int amount) {
        currentIntake += amount;

        // Prevent exceeding goal
        if (currentIntake > dailyGoal) {
            currentIntake = dailyGoal;
        }

        // Save updated value with today's date
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        prefs.edit()
                .putInt("water_ml", currentIntake)
                .putString("water_date", today)
                .apply();

        updateUI();
    }

    private void updateUI() {
        // Update circular progress (ml units)
        waterProgress.setProgress(currentIntake, true);

        // Update text
        tvWaterAmount.setText(currentIntake + " ml");

        int progressPercent = (currentIntake * 100) / dailyGoal;
        tvHydrationPercent.setText(progressPercent + "% Hydrated 💧");
    }
}
