package com.project.realhealthbuddy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.project.realhealthbuddy.R;

public class WaterReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String channelId = "water_reminder_channel";

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Water Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.hydration_wellness) // use your icon
                .setContentTitle("Time to Drink Water 💧")
                .setContentText("Stay hydrated for better health!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(1, builder.build());
    }
}