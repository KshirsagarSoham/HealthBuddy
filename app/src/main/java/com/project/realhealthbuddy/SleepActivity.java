package com.project.realhealthbuddy;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.Calendar;

public class SleepActivity extends AppCompatActivity {

    TextView tvSleepTime, tvWakeTime, tvSleepDuration, tvSleepAdvice;
    ImageView ivBack;
    MaterialButton btnCalculateSleep;
    MaterialCardView cardResult;

    int sleepHour = -1, sleepMinute = -1;
    int wakeHour = -1, wakeMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        // Bind views
        tvSleepTime = findViewById(R.id.tvSleepTime);
        tvWakeTime = findViewById(R.id.tvWakeTime);
        tvSleepDuration = findViewById(R.id.tvSleepDuration);
        btnCalculateSleep = findViewById(R.id.btnCalculateSleep);
        cardResult = findViewById(R.id.cardResult);
        tvSleepAdvice = findViewById(R.id.tvSleepAdvice);
        ivBack = findViewById(R.id.backbutton);


        // Sleep time picker
        tvSleepTime.setOnClickListener(v -> openSleepTimePicker());

        // Wake-up time picker
        tvWakeTime.setOnClickListener(v -> openWakeTimePicker());

        // Calculate button
        btnCalculateSleep.setOnClickListener(v -> calculateSleep());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // ------------------ TIME PICKERS ------------------

    private void openSleepTimePicker() {
        Calendar c = Calendar.getInstance();

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    sleepHour = hourOfDay;
                    sleepMinute = minute;
                    tvSleepTime.setText(formatTime(hourOfDay, minute));
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        dialog.show();
    }

    private void openWakeTimePicker() {
        Calendar c = Calendar.getInstance();

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    wakeHour = hourOfDay;
                    wakeMinute = minute;
                    tvWakeTime.setText(formatTime(hourOfDay, minute));
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        dialog.show();
    }

    // ------------------ CALCULATION ------------------

    private void calculateSleep() {

        if (sleepHour == -1 || wakeHour == -1) {
            Toast.makeText(this, "Please select both times", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar sleepCal = Calendar.getInstance();
        Calendar wakeCal = Calendar.getInstance();

        sleepCal.set(Calendar.HOUR_OF_DAY, sleepHour);
        sleepCal.set(Calendar.MINUTE, sleepMinute);

        wakeCal.set(Calendar.HOUR_OF_DAY, wakeHour);
        wakeCal.set(Calendar.MINUTE, wakeMinute);

        // If wake time is before sleep time → next day
        if (wakeCal.before(sleepCal)) {
            wakeCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        long diffMillis = wakeCal.getTimeInMillis() - sleepCal.getTimeInMillis();

        int hours = (int) (diffMillis / (1000 * 60 * 60));
        int minutes = (int) ((diffMillis / (1000 * 60)) % 60);

        int totalMinutes = hours * 60 + minutes;

        tvSleepDuration.setText(hours + "h " + minutes + "m");

        String advice = getSleepAdvice(totalMinutes);
        tvSleepAdvice.setText(advice);

        cardResult.setVisibility(View.VISIBLE);

    }

    // ------------------ TIME FORMAT ------------------

    private String formatTime(int hour, int minute) {
        String amPm = hour >= 12 ? "PM" : "AM";
        int hour12 = hour % 12;
        if (hour12 == 0) hour12 = 12;

        return String.format("%02d:%02d %s", hour12, minute, amPm);
    }

    private String getSleepAdvice(int totalMinutes) {

        double hours = totalMinutes / 60.0;

        if (hours >= 7) {
            return "🌟 You had a healthy sleep. Keep it up!";
        } else if (hours >= 6) {
            return "🙂 Your sleep was average. Try to rest a bit more.";
        } else {
            return "⚠️ Poor sleep detected. Your body needs more rest.";
        }
    }

}
