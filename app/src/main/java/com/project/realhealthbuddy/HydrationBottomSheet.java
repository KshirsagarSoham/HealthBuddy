package com.project.realhealthbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class HydrationBottomSheet extends BottomSheetDialogFragment {

    Button btn30min, btn1hr, btnDrinkNow, btnCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_hydration, container, false);

        btn30min = view.findViewById(R.id.btn30min);
        btn1hr = view.findViewById(R.id.btn1hr);
        btnDrinkNow = view.findViewById(R.id.btnDrinkNow);
        btnCancel = view.findViewById(R.id.btnCancel);

        btn30min.setOnClickListener(v -> setReminder(30));
        btn1hr.setOnClickListener(v -> setReminder(60));

        btnDrinkNow.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), WaterActivity.class));
            dismiss();
        });

        btnCancel.setOnClickListener(v -> cancelReminder());

        // 🔍 Show/hide cancel button
        SharedPreferences prefs = requireContext().getSharedPreferences("water_reminder", Context.MODE_PRIVATE);
        long time = prefs.getLong("reminder_time", 0);

        if (time > System.currentTimeMillis()) {
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnCancel.setVisibility(View.GONE);
        }

        return view;
    }

    private void setReminder(int minutes) {

        Context context = requireContext();

        Intent intent = new Intent(context, WaterReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                minutes,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000);

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
        );

        // 💾 Save reminder
        SharedPreferences prefs = context.getSharedPreferences("water_reminder", Context.MODE_PRIVATE);
        prefs.edit()
                .putLong("reminder_time", triggerTime)
                .putInt("reminder_minutes", minutes)
                .putInt("request_code", minutes)
                .apply();

        dismiss();

        // ✅ Correct Snackbar (no crash)
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                "Reminder set for " + minutes + " minutes ⏰",
                Snackbar.LENGTH_LONG).show();
    }

    private void cancelReminder() {

        Context context = requireContext();

        SharedPreferences prefs = context.getSharedPreferences("water_reminder", Context.MODE_PRIVATE);
        int requestCode = prefs.getInt("request_code", -1);

        if (requestCode == -1) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "No active reminder 🤷",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(context, WaterReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        // 🧹 Clear data
        prefs.edit().clear().apply();

        dismiss();

        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                "Reminder cancelled ❌",
                Snackbar.LENGTH_SHORT).show();
    }
}