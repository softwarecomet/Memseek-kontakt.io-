package com.example.memseekandroid.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.LogPrinter;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        int notificationId = intent.getIntExtra("notificationId", 0);

        String id = Integer.toString(notificationId);
        Log.e ("sdadfadsfasdfa",id);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
