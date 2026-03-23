package com.project.realhealthbuddy.Steps.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StepsDao {

    // Insert new day record
    @Insert
    void insert(StepsEntity entity);

    // Update existing day record
    @Update
    void update(StepsEntity entity);

    // Get steps of specific date
    @Query("SELECT * FROM steps_table WHERE date = :date LIMIT 1")
    StepsEntity getStepsByDate(String date);


        //  NEW: LiveData for auto-refresh
        @Query("SELECT * FROM steps_table ORDER BY date DESC LIMIT 7")
        LiveData<List<StepsEntity>> getLast7DaysLive();


        // FIXED: Get ALL last 7 days (not just 3)
        @Query("SELECT * FROM steps_table WHERE date >= date('now', '-7 days') ORDER BY date DESC")
        List<StepsEntity> getLast7Days();

        // OR use this simpler version:

    }

