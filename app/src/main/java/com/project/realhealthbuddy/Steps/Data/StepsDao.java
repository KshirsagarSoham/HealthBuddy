package com.project.realhealthbuddy.Steps.Data;

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

    // Get last 7 days (for future weekly display)
    @Query("SELECT * FROM steps_table ORDER BY date DESC LIMIT 7")
    List<StepsEntity> getLast7Days();
}
