package com.ping.sleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SleepAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String quote = QuoteManager.getRandomQuote(context);
        NotificationHelper.showSleepReminder(context, quote);
    }
}
