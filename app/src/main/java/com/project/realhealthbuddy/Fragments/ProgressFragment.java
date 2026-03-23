package com.project.realhealthbuddy.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.project.realhealthbuddy.R;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.Model.Medicine;
import com.project.realhealthbuddy.Steps.Repository.HealthRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressFragment extends Fragment {
    private HealthRepository repository;
    private LinearLayout stepsContainer, medicineContainer;
    private TextView tvTodaySteps, tvWeeklyGoal, tvTodayTaken, tvDate;
    private ProgressBar pbWeeklyProgress;
    SharedPreferences preferences;
    private String todayDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        initViews(view);

        loadData();
        return view;
    }

    private void initViews(View view) {
        stepsContainer = view.findViewById(R.id.steps_container);
        medicineContainer = view.findViewById(R.id.medicine_container);
        tvTodaySteps = view.findViewById(R.id.tv_today_steps);
        tvWeeklyGoal = view.findViewById(R.id.tv_weekly_goal);
        tvTodayTaken = view.findViewById(R.id.tv_today_taken);
        tvDate = view.findViewById(R.id.tv_date);
        pbWeeklyProgress = view.findViewById(R.id.pb_weekly_progress);
    }

    private void loadData() {
        repository = new HealthRepository(requireContext());
        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvDate.setText(new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(new Date()));

        // Load Steps (from your StepsActivity Room DB)
        new Thread(() -> {
            List<StepsEntity> last7Days = repository.getLast7Days();
            StepsEntity todaySteps = repository.getTodaySteps(todayDate);

            requireActivity().runOnUiThread(() -> {
                tvTodaySteps.setText(todaySteps != null ? String.valueOf(todaySteps.steps) : "0");
                loadWeeklySteps(last7Days);
                updateWeeklyProgress(last7Days);
            });
        }).start();

        // Load Medicines (from MedicineFragment SharedPreferences)
        List<Medicine> medicines = repository.getTodayMedicines(requireContext());
        loadMedicines(medicines);
    }

    private void loadWeeklySteps(List<StepsEntity> dbSteps) {
        stepsContainer.removeAllViews();

        // Create map of your DB steps (date → steps)
        java.util.Map<String, Integer> stepsMap = new java.util.HashMap<>();
        for (StepsEntity step : dbSteps) {
            stepsMap.put(step.date, step.steps);
        }

        // Get today for highlighting
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Start from Sunday of CURRENT WEEK
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        // Generate 7 days: Sun→Sat with YOUR real data
        for (int i = 0; i < 7; i++) {
            Calendar dayCal = (Calendar) cal.clone();
            String dayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dayCal.getTime());
            String dayName = new SimpleDateFormat("EEE", Locale.getDefault()).format(dayCal.getTime());
            int steps = stepsMap.containsKey(dayDate) ? stepsMap.get(dayDate) : 0;
            boolean isToday = dayDate.equals(todayDate);

            createStepsColumn(dayDate, dayName, steps, isToday);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

//    private void createStepsColumn(String date, String dayName, int steps, boolean isToday) {
//        LinearLayout column = new LinearLayout(requireContext());
//        column.setOrientation(LinearLayout.VERTICAL);
//        column.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
//        column.setPadding(12, 12, 12, 12);
//
//        if (isToday) {
//            column.setBackground(getResources().getDrawable(R.drawable.bg_today_highlight));
//        }
//
//        // Progress bar (10K max)
//        ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
//        pb.setMax(10000);
//        pb.setProgress(steps);
//        pb.setProgressTintList(getResources().getColorStateList(android.R.color.holo_green_light));
//        pb.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
//        pb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
//
//        // Steps count
//        TextView stepsTv = new TextView(requireContext());
//        stepsTv.setText(String.valueOf(steps));
//        stepsTv.setTextColor(isToday ?
//                getResources().getColor(android.R.color.black) :
//                getResources().getColor(android.R.color.white));
//        stepsTv.setTextSize(16);
//        stepsTv.setTypeface(null, isToday ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
//        stepsTv.setGravity(android.view.Gravity.CENTER);
//
//        // Day name
//        TextView dayTv = new TextView(requireContext());
//        dayTv.setText(dayName);
//        dayTv.setTextColor(isToday ?
//                getResources().getColor(android.R.color.black) :
//                getResources().getColor(android.R.color.white));
//        dayTv.setTextSize(14);
//        dayTv.setTypeface(null, isToday ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
//        dayTv.setGravity(android.view.Gravity.CENTER);
//
//        column.addView(pb);
//        column.addView(stepsTv);
//        column.addView(dayTv);
//        stepsContainer.addView(column);
//    }

    private void createStepsColumn(String date, String dayName, int steps, boolean isToday) {
        LinearLayout column = new LinearLayout(requireContext());
        column.setOrientation(LinearLayout.VERTICAL);
        column.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        column.setPadding(12, 12, 12, 12);

        if (isToday) {
            column.setBackground(getResources().getDrawable(R.drawable.bg_today_highlight, null));
        }

        // Progress Bar (0-10000 max)
        ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);

        int dailygoal =repository.getDailyGoal(requireContext());
//        int maxSteps = 7 * dailygoal;  // Always 70K for 7 days
        pb.setMax(dailygoal);
        pb.setProgress(steps);

        // Color based on progress
        if (steps >= steps*85/100) {
            pb.setProgressTintList(getResources().getColorStateList(android.R.color.holo_green_light));
        } else if (steps >= steps*50/100) {
            pb.setProgressTintList(getResources().getColorStateList(android.R.color.holo_orange_light));
        } else {
            pb.setProgressTintList(getResources().getColorStateList(android.R.color.holo_red_light));
        }

        pb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));

        // Steps Count
        TextView stepsTv = new TextView(requireContext());
        stepsTv.setText(String.valueOf(steps));
        stepsTv.setTextColor(isToday ?
                getResources().getColor(android.R.color.holo_purple) :
                getResources().getColor(android.R.color.darker_gray));
        stepsTv.setTextSize(16);
        stepsTv.setTypeface(null, isToday ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        stepsTv.setGravity(android.view.Gravity.CENTER);

        // Day Name
        TextView dayTv = new TextView(requireContext());
        dayTv.setText(dayName);
        dayTv.setTextColor(isToday ?
                getResources().getColor(android.R.color.holo_purple) :
                getResources().getColor(android.R.color.darker_gray));
        dayTv.setTextSize(14);
        dayTv.setTypeface(null, isToday ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        dayTv.setGravity(android.view.Gravity.CENTER);

        column.addView(pb);
        column.addView(stepsTv);
        dayTv.setGravity(android.view.Gravity.CENTER);
        column.addView(dayTv);
        stepsContainer.addView(column);
    }

    private void updateWeeklyProgress(List<StepsEntity> steps) {
        int totalSteps = 0;
        for (StepsEntity step : steps) {
            totalSteps += step.steps;
        }
        int dailygoal =repository.getDailyGoal(requireContext());

        int maxSteps = 7 * dailygoal;  // Always 70K for 7 days
        pbWeeklyProgress.setMax(maxSteps);
        pbWeeklyProgress.setProgress(totalSteps);
        tvWeeklyGoal.setText("Weekly Goal: "+maxSteps+" steps");
    }

    private void createStepsColumn(StepsEntity step, String dayName) {
        LinearLayout column = new LinearLayout(requireContext());
        column.setOrientation(LinearLayout.VERTICAL);
        column.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        column.setPadding(8, 8, 8, 8);

        int goal= 300;
        // Progress Bar
        ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
        pb.setMax(goal);
        pb.setProgress(step.steps);
        pb.setProgressTintList(getResources().getColorStateList(R.color.purple));
        pb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60));

        // Steps Count
        TextView stepsTv = new TextView(requireContext());
        stepsTv.setText(String.valueOf(step.steps));
        stepsTv.setTextColor(getResources().getColor(android.R.color.white));
        stepsTv.setTextSize(14);
        stepsTv.setTypeface(null, android.graphics.Typeface.BOLD);
        stepsTv.setGravity(android.view.Gravity.CENTER);

        // Day Name
        TextView dayTv = new TextView(requireContext());
        dayTv.setText(dayName);
        dayTv.setTextColor(getResources().getColor(android.R.color.white));
        dayTv.setTextSize(12);
        dayTv.setGravity(android.view.Gravity.CENTER);

        column.addView(pb);
        column.addView(stepsTv);
        column.addView(dayTv);
        stepsContainer.addView(column);
    }

    private void loadMedicines(List<Medicine> medicines) {
        medicineContainer.removeAllViews();
        int takenCount = 0, totalDoses = 0;

        for (Medicine medicine : medicines) {
            totalDoses += medicine.getTimings().size();
            takenCount += medicine.getTakenCount();
            createMedicineItem(medicine);
        }
        tvTodayTaken.setText("Today: " + takenCount + "/" + totalDoses + " taken");
    }

    private void createMedicineItem(Medicine medicine) {
        LinearLayout item = new LinearLayout(requireContext());
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setPadding(20, 16, 20, 16);
        item.setBackgroundResource(R.drawable.bg_medicine_pending);

        // Icon
        ImageView icon = new ImageView(requireContext());
        icon.setImageResource(android.R.drawable.ic_menu_info_details);
        icon.setColorFilter(getResources().getColor(android.R.color.white));
        icon.setLayoutParams(new LinearLayout.LayoutParams(48, 48));

        // Details
        LinearLayout details = new LinearLayout(requireContext());
        details.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        TextView nameTv = new TextView(requireContext());
        nameTv.setText(medicine.getName() + " (" + medicine.getDosage() + ")");
        nameTv.setTextColor(getResources().getColor(android.R.color.white));
        nameTv.setTextSize(16);
        nameTv.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView timingsTv = new TextView(requireContext());
        StringBuilder timings = new StringBuilder();
        for (int i = 0; i < Math.min(medicine.getTimings().size(), 2); i++) {
            timings.append(medicine.getTimings().get(i));
            if (i < medicine.getTimings().size() - 1) timings.append(", ");
        }
        if (medicine.getTimings().size() > 2) timings.append("...");
        timingsTv.setText(timings.toString());
        timingsTv.setTextColor(getResources().getColorStateList(android.R.color.darker_gray));
        timingsTv.setTextSize(14);

        details.addView(nameTv);
        details.addView(timingsTv);
        details.setLayoutParams(detailsParams);

        item.addView(icon);
        item.addView(details);
        medicineContainer.addView(item);
    }


}