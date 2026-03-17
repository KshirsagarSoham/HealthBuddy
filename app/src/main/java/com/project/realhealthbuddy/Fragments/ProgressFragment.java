package com.project.realhealthbuddy.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.project.realhealthbuddy.R;

public class ProgressFragment extends Fragment {
    private TextView tvStepsToday, tvDailyGoal, tvMedicineMorning, tvMedicineLunch;
    private ProgressBar pbSteps;
    private LinearLayout dayProgressContainer;  // ← ADDED: For 7-day bars
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        initViews(view);
        loadDynamicData();
        loadWeeklyData();  // ← ADDED: Load 7-day data
        animateEntrance();
        return view;
    }

    private void initViews(View view) {
        tvStepsToday = view.findViewById(R.id.tv_steps_today);
        tvDailyGoal = view.findViewById(R.id.tv_daily_goal);
        pbSteps = view.findViewById(R.id.pb_steps);
        tvMedicineMorning = view.findViewById(R.id.tv_medicine_morning);
        tvMedicineLunch = view.findViewById(R.id.tv_medicine_lunch);
        dayProgressContainer = view.findViewById(R.id.day_progress_container);  // ← ADDED
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    private void loadDynamicData() {
        int stepsToday = prefs.getInt("steps_today", 8200);
        int dailyGoal = 10000;
        tvStepsToday.setText(String.valueOf(stepsToday));
        tvDailyGoal.setText("Daily Goal: " + dailyGoal);

        int progress = (int) ((stepsToday * 100f) / dailyGoal);
        pbSteps.setProgress(progress);
        pbSteps.setMax(100);

        updateMedicineStatus("morning_pill", tvMedicineMorning, "Morning Pill (8:00 AM)");
        updateMedicineStatus("lunch_supplement", tvMedicineLunch, "Lunch Supplement (1:00 PM)");
    }

    // ← ADDED: Dynamic 7-day progress bars (matches your screenshot)
    private void loadWeeklyData() {
        dayProgressContainer.removeAllViews();
        int[] weeklySteps = {7300, 4200, 9800, 3500, 4900, 7200, 8200};
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Today"};

        for (int i = 0; i < weeklySteps.length; i++) {
            createDayProgressBar(days[i], weeklySteps[i]);
        }
    }

    // ← ADDED: Creates each day's progress bar
    private void createDayProgressBar(String day, int steps) {
        LinearLayout dayLayout = new LinearLayout(requireContext());
        dayLayout.setOrientation(LinearLayout.VERTICAL);
        dayLayout.setPadding(0, 8, 0, 8);

        // Progress bar
        ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
        pb.setMax(10000);
        pb.setProgress(steps);
        pb.setProgressTintList(getResources().getColorStateList(android.R.color.holo_green_light));
        LinearLayout.LayoutParams pbParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 12);
        pb.setLayoutParams(pbParams);

        // Day label
        TextView tv = new TextView(requireContext());
        tv.setText(day + ": " + steps);
        tv.setTextColor(getResources().getColor(android.R.color.white));
        tv.setTextSize(14);
        tv.setPadding(0, 4, 0, 0);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(tvParams);

        dayLayout.addView(pb);
        dayLayout.addView(tv);
        dayProgressContainer.addView(dayLayout);
    }

    private void updateMedicineStatus(String key, TextView tv, String label) {
        boolean taken = prefs.getBoolean(key, false);
        tv.setText(label + (taken ? " ✓" : " ⏳"));
        tv.setBackgroundResource(taken ? R.drawable.bg_medicine_taken : R.drawable.bg_medicine_pending);
    }

    private void animateEntrance() {
        // Pulse animation for steps circle (no Lottie needed)
        ValueAnimator pulse = ValueAnimator.ofFloat(0.95f, 1.05f);
        pulse.setDuration(1200);
        pulse.setRepeatCount(ValueAnimator.INFINITE);
        pulse.setRepeatMode(ValueAnimator.REVERSE);
        pulse.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            pbSteps.setScaleX(scale);
            pbSteps.setScaleY(scale);
        });
        pulse.start();

        // Fade in
        tvStepsToday.setAlpha(0f);
        tvStepsToday.animate().alpha(1f).setDuration(800).start();
    }

    public void updateSteps(int newSteps) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("steps_today", newSteps);
        editor.apply();
        loadDynamicData();
        loadWeeklyData();  // Refresh weekly too
    }
}