package com.project.realhealthbuddy.Steps;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
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

public class StepsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private boolean isSensorPresent = false;

    private float totalSteps = 0f;

    private TextView stepsText;

    private StepsRepository repository;
    private StepsEntity todayEntity;

    private String todayDate;

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

            if (todayEntity == null) {

                // First time today → save baseline
                todayEntity = new StepsEntity(todayDate, totalSteps, 0);
                repository.insert(todayEntity);

                stepsText.setText("0");

            } else {

                int todaySteps = (int) (totalSteps - todayEntity.baseValue);

                if (todaySteps < 0) {
                    todaySteps = 0; // safety check if phone rebooted
                }

                todayEntity.steps = todaySteps;
                repository.update(todayEntity);

                stepsText.setText(String.valueOf(todaySteps));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
