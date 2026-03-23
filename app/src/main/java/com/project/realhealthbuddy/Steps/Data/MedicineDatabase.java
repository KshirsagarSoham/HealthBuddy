package com.project.realhealthbuddy.Steps.Data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MedicineEntity.class}, version = 1, exportSchema = false)
public abstract class MedicineDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();

    private static volatile MedicineDatabase INSTANCE;

    public static MedicineDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MedicineDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                MedicineDatabase.class, "medicine_database")
                        .build();
            }
        }
        return INSTANCE;
    }
}