package com.project.realhealthbuddy.Steps.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.realhealthbuddy.Steps.Data.StepsDao;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.comman.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StepsRepository {


    // Get user's daily goal from SharedPreferences
        public int getDailyGoal(Context context) {
            SharedPreferences prefs = context.getSharedPreferences("steps_prefs", Context.MODE_PRIVATE);
            return prefs.getInt("step_goal", 8000);  // Default 8K
        }


    private final StepsDao stepsDao;
    private final ExecutorService executorService;

    public StepsRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        stepsDao = db.stepsDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Insert new record
    public void insert(StepsEntity entity) {
        executorService.execute(() -> stepsDao.insert(entity));
    }

    // Update record
    public void update(StepsEntity entity) {
        executorService.execute(() -> stepsDao.update(entity));
    }

    public List<StepsEntity> getLast7DaysWithDays() {
        return stepsDao.getLast7Days();
    }

    // Get record by date (synchronous for now because allowMainThreadQueries is enabled)
    public StepsEntity getStepsByDate(String date) {
        return stepsDao.getStepsByDate(date);
    }


        // Return exactly 7 days (pad with zeros if needed)
        public List<StepsEntity> getLast7Days() {
            List<StepsEntity> steps = stepsDao.getLast7Days();
            // Pad to 7 if less
            while (steps.size() < 7) {
                steps.add(new StepsEntity("", 0, 0));
            }
            return steps;
        }

}
