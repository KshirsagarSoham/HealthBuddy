package com.project.realhealthbuddy.Steps;

import android.app.*;
import android.content.Context;
import android.hardware.*;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.Steps.Repository.StepsRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private StepsRepository repository;
    private StepsEntity todayEntity;
    private String todayDate;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new StepsRepository(this);

        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        todayEntity = repository.getStepsByDate(todayDate);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        startForeground(1, createNotification());
    }

    private Notification createNotification() {

        String channelId = "step_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Step Tracker",
                    NotificationManager.IMPORTANCE_LOW
            );

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Tracking Steps 👣")
                .setContentText("Your steps are being tracked")
                .setSmallIcon(R.drawable.steps) // make sure this icon exists
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSilent(true)
                .build();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            float totalSteps = event.values[0];
            int todaySteps;

            if (todayEntity == null) {
                todayEntity = new StepsEntity(todayDate, totalSteps, 0);
                repository.insert(todayEntity);
                todaySteps = 0;

            } else {

                if (totalSteps < todayEntity.baseValue) {
                    todayEntity.baseValue = totalSteps;
                    todaySteps = 0;
                } else {
                    todaySteps = (int) (totalSteps - todayEntity.baseValue);
                }

                todayEntity.steps = todaySteps;
                repository.update(todayEntity);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for step counter, leave empty
    }

    @Override
    public IBinder onBind(android.content.Intent intent) {
        return null;
    }
}