package com.ping.sleep;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    private static final String CHANNEL_ID = "ping_reminder_channel";
    private static final int NOTIFICATION_ID = 1990;

    public static void showSleepReminder(Context context, String quote) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "平平的提醒",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("平平提醒你该睡了");
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setContentTitle("PING2.0 平平")
                .setContentText(quote)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(0xFFE6B800);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
