package com.project.realhealthbuddy.Steps.Data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps_table")
public class StepsEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;        // yyyy-MM-dd
    public float baseValue;    // First sensor value of that day
    public int steps;          // Calculated daily steps

    public StepsEntity(String date, float baseValue, int steps) {
        this.date = date;
        this.baseValue = baseValue;
        this.steps = steps;
    }
}
