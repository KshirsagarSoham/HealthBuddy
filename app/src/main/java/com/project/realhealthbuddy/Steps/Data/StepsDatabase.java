package com.project.realhealthbuddy.Steps.Data;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {StepsEntity.class}, version = 1, exportSchema = false)
public abstract class StepsDatabase extends RoomDatabase {
    public abstract StepsDao stepsDao();

    private static volatile StepsDatabase INSTANCE;

    public static StepsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StepsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    StepsDatabase.class, "steps_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}