package com.project.realhealthbuddy.Steps;

import android.app.*;
import android.content.Context;
import android.content.Intent;
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

    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new StepsRepository(this);

        // ✅ Initialize date formatter once (optimized)
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        todayDate = dateFormat.format(new Date());
        todayEntity = repository.getStepsByDate(todayDate);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        startForeground(1, createNotification());
        scheduleMidnightRestart();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && "RESTART_SERVICE".equals(intent.getAction())) {
            stopSelf(); // 🔥 stop old instance
            startService(new Intent(this, StepService.class)); // start fresh
            return START_STICKY;
        }

        scheduleMidnightRestart(); // normal scheduling
        return START_STICKY;
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
                .setOngoing(true)
                .setContentTitle("Tracking Steps 👣")
                .setContentText("Your steps are being tracked")
                .setSmallIcon(R.drawable.steps) // ensure this icon exists
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSilent(true)
                .build();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() != Sensor.TYPE_STEP_COUNTER) return;

        // ✅ Check date on every update (fix midnight issue)
        String currentDate = dateFormat.format(new Date());

        if (!currentDate.equals(todayDate)) {
            todayDate = currentDate;
            todayEntity = null; // 🔥 force fresh start for new day
        }

        float totalSteps = event.values[0];
        int todaySteps;

        if (todayEntity == null) {
            // First record of the day → set baseline
            todayEntity = new StepsEntity(todayDate, totalSteps, 0);
            repository.insert(todayEntity);
            todaySteps = 0;

        } else {

            if (totalSteps < todayEntity.baseValue) {
                // Device reboot case
                todayEntity.baseValue = totalSteps;
                todaySteps = 0;
            } else {
                todaySteps = (int) (totalSteps - todayEntity.baseValue);
            }

            todayEntity.steps = todaySteps;
            repository.update(todayEntity);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for step counter
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // ✅ Prevent sensor leak
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public IBinder onBind(android.content.Intent intent) {
        return null;
    }

    private void scheduleMidnightRestart() {

        android.app.AlarmManager alarmManager =
                (android.app.AlarmManager) getSystemService(Context.ALARM_SERVICE);

        android.content.Intent intent =
                new android.content.Intent(this, StepService.class);

        intent.setAction("RESTART_SERVICE");

        android.app.PendingIntent pendingIntent =
                android.app.PendingIntent.getService(
                        this,
                        1,
                        intent,
                        android.app.PendingIntent.FLAG_IMMUTABLE | android.app.PendingIntent.FLAG_UPDATE_CURRENT
                );

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Set to next midnight
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);

        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    android.app.AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
}