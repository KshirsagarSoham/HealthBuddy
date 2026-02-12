package com.project.realhealthbuddy.comman;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.project.realhealthbuddy.Steps.Data.StepsEntity;
import com.project.realhealthbuddy.Steps.Data.StepsDao;

@Database(
        entities = {User.class, StepsEntity.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract StepsDao stepsDao();

    // Singleton
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "InstallationAppDB"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // We will remove later
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
