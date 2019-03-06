package com.example.bonaventurajason.mycataloguemoviebd.Notif;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.bonaventurajason.mycataloguemoviebd.MainActivity;
import com.example.bonaventurajason.mycataloguemoviebd.R;


public class ReminderIntent extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "1";
    private NotificationCompat.Builder notification;

    public ReminderIntent() {
        super(ReminderIntent.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        pushReminderNotification(getResources().getString(R.string.content_text));
    }

    private void pushReminderNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//      Builder for notif

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle(getResources().getString(R.string.content_title))
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }
}
