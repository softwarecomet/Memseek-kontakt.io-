package com.example.memseekandroid.Activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    final BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onReceive(Context context, Intent intent) {


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
         if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)
             NotificationHelper notificationHelper = new NotificationHelper(context);
             notificationHelper.createNotification();
         }
    }
}
