package com.project.realhealthbuddy.comman;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.project.realhealthbuddy.R;

// NOTE: There is already a 'comman' package in your project.
// We are placing this receiver there to avoid creating new packages.
// If you prefer a different location, move it and update the manifest accordingly.

public class MedicineAlarmReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID       = "medicine_alarm_channel";
    public static final String EXTRA_MED_NAME   = "medicine_name";
    public static final String EXTRA_MED_DOSAGE = "medicine_dosage";
    public static final String EXTRA_TIMING     = "timing";
    public static final String EXTRA_NOTIF_ID   = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String name    = intent.getStringExtra(EXTRA_MED_NAME);
        String dosage  = intent.getStringExtra(EXTRA_MED_DOSAGE);
        String timing  = intent.getStringExtra(EXTRA_TIMING);
        int    notifId = intent.getIntExtra(EXTRA_NOTIF_ID, 0);

        createChannel(context);
        showNotification(context, name, dosage, timing, notifId);
    }

    private void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm =
                    context.getSystemService(NotificationManager.class);
            if (nm.getNotificationChannel(CHANNEL_ID) != null) return;

            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Reminders",
                    NotificationManager.IMPORTANCE_HIGH);
            ch.setDescription("Reminds you to take your medicine on time");
            ch.enableVibration(true);
            ch.setVibrationPattern(new long[]{0, 500, 200, 500});
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ch.setSound(sound, null);
            nm.createNotificationChannel(ch);
        }
    }

    private void showNotification(Context context, String name,
                                  String dosage, String timing, int notifId) {
        // Open app on notification tap
        Intent open = new Intent(context,
                com.project.realhealthbuddy.MainActivity.class);
        open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(
                context, notifId, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_medicine_notify)
                        .setContentTitle("💊 Time to take " + name)
                        .setContentText("Dosage: " + dosage + "  ·  Time: " + timing)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("It's time to take your " + name
                                        + "\nDosage: " + dosage
                                        + "\nScheduled: " + timing))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setSound(sound)
                        .setVibrate(new long[]{0, 500, 200, 500})
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setColor(0xFF5D96E2);  // Azure Mist

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notifId, builder.build());
    }
}