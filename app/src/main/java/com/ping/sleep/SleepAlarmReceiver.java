package com.ping.sleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class SleepAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("ping_prefs", Context.MODE_PRIVATE);

        // 发送通知
        String quote = QuoteManager.getRandomQuote(context, prefs);
        NotificationHelper.showNotification(context, quote);

        // 计算下一次触发时间
        int interval = prefs.getInt("interval_minutes", 30);
        long nextTime = System.currentTimeMillis() + interval * 60 * 1000L;

        // 获取当天开始时间
        int hour = prefs.getInt("start_hour", 23);
        int minute = prefs.getInt("start_minute", 0);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long todayStart = cal.getTimeInMillis();
        long tomorrowStart = todayStart + 24 * 60 * 60 * 1000L;

        // 如果下一次时间超过了明天开始时间，则重置为明天开始时间
        if (nextTime > tomorrowStart) {
            nextTime = tomorrowStart;
        }

        // 设置下一次闹钟
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent nextIntent = new Intent(context, SleepAlarmReceiver.class);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                context, 0, nextIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
        );
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
        } else {
            alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
        }
    }
}
