package com.project.realhealthbuddy.Steps.Data;



import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine_table")
public class MedicineEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;        // yyyy-MM-dd
    public String name;        // "Metformin", "Lisinopril"
    public String time;        // "8:00 AM"
    public double dose;        // 500.0
    public String unit;        // "mg"
    public boolean taken;      // true/false

    public MedicineEntity(String date, String name, String time, double dose, String unit) {
        this.date = date;
        this.name = name;
        this.time = time;
        this.dose = dose;
        this.unit = unit;
        this.taken = false;
    }
}