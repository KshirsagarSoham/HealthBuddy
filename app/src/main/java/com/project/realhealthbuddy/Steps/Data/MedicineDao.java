package com.project.realhealthbuddy.Steps.Data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM medicine_table WHERE date = :date ORDER BY time")
    LiveData<List<MedicineEntity>> getTodayMedicines(String date);

    @Insert
    void insert(MedicineEntity entity);

    @Update
    void update(MedicineEntity entity);
}