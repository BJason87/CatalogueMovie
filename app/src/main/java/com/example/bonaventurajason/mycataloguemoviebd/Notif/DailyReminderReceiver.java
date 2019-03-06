package com.example.bonaventurajason.mycataloguemoviebd.Notif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DailyReminderReceiver extends BroadcastReceiver {

    private static final String TAG = DailyReminderReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent reminderIntent = new Intent(context, ReminderIntent.class);
        context.startService(reminderIntent);
    }
}
