package com.project.realhealthbuddy.Steps.Repository;

import android.content.Context;

import com.project.realhealthbuddy.Steps.Data.StepsDao;
import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.comman.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StepsRepository {

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

    // Get record by date (synchronous for now because allowMainThreadQueries is enabled)
    public StepsEntity getStepsByDate(String date) {
        return stepsDao.getStepsByDate(date);
    }

    // Get last 7 days
    public List<StepsEntity> getLast7Days() {
        return stepsDao.getLast7Days();
    }
}
