package com.example.bonaventurajason.mycataloguemoviebd.Notif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReleaseTodayReceiver extends BroadcastReceiver {
    private static final String TAG = ReleaseTodayReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Intent ke Release Intent
        Intent releaseIntent = new Intent(context, ReleaseIntent.class);
        context.startService(releaseIntent);
    }
}
