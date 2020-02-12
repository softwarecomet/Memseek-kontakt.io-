package com.example.memseekandroid.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.memseekandroid.Activity.BackgroundActivity;
import com.example.memseekandroid.Activity.MainActivity;
import com.example.memseekandroid.R;
import com.example.memseekandroid.Utils.NotificationReceiver;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;

import java.security.Provider;
import java.util.Random;

public class BackgroundService extends Service{

    public static final String TAG = BackgroundService.class.getSimpleName();

    public static final String ACTION_DEVICE_DISCOVERED = "DEVICE_DISCOVERED_ACTION";
    public static final String ACTION_DEVICE_LOSTED="DEVICE_LOSTED_ACTION";
    public static final String EXTRA_DEVICE = "DeviceExtra";
    public static final String EXTRA_DEVICES_COUNT = "DevicesCountExtra";
    private static final String STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION";

    private ProximityManager proximityManager;
    private boolean isRunning; // Flag indicating if service is already running.
    private int devicesCount; // Total discovered devices count

    public static Intent createIntent(BackgroundActivity context) {
        return new Intent(context,BackgroundService.class);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        setupProximityManager();
        isRunning = false;
    }
    private void setupProximityManager() {
        // Create proximity manager instance
        proximityManager = ProximityManagerFactory.create(this);

        // Configure proximity manager basic options
        proximityManager.configuration()
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                //Using BALANCED for best performance/battery ratio
                .scanMode(ScanMode.BALANCED);

        // Set up iBeacon listener
        proximityManager.setIBeaconListener(createIBeaconListener());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (STOP_SERVICE_ACTION.equals(intent.getAction())) {
            stopSelf();
            return START_NOT_STICKY;
        }

        // Check if service is already active
        if (isRunning) {
            Toast.makeText(this, "Service is already running.", Toast.LENGTH_SHORT).show();

            return START_STICKY;
        }

//    startInBackground();
        startScanning();
        isRunning = true;
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (proximityManager != null) {
            proximityManager.disconnect();
            proximityManager = null;
        }
        Toast.makeText(this, "Scanning service stopped.", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
                devicesCount = 0;
                Toast.makeText(BackgroundService.this, "Scanning service started.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {


                onDeviceDiscovered(ibeacon);
                Log.i(TAG, "onIBeaconDiscovered: " + ibeacon.toString());
                Toast.makeText(BackgroundService.this, "beacon was detected.", Toast.LENGTH_SHORT).show();
                headsUpNotification();
            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {

                onDeviceLosted(ibeacon);
                super.onIBeaconLost(ibeacon, region);
                Log.e(TAG, "onIBeaconLost: " + ibeacon.toString());
                Toast.makeText(BackgroundService.this, "beacon Lost", Toast.LENGTH_SHORT).show();

            }
        };
    }
    private void onDeviceDiscovered(final RemoteBluetoothDevice device) {
        devicesCount++;
        //Send a broadcast with discovered device
        Intent intent = new Intent();
        intent.setAction(ACTION_DEVICE_DISCOVERED);
        intent.putExtra(EXTRA_DEVICE, device);
        intent.putExtra(EXTRA_DEVICES_COUNT, devicesCount);
        sendBroadcast(intent);
    }
    private void onDeviceLosted(final RemoteBluetoothDevice device){
        //Send a broadcast with losted device
        Intent intent = new Intent();
        intent.setAction(ACTION_DEVICE_LOSTED);
        intent.putExtra(EXTRA_DEVICE, device);
        intent.putExtra(EXTRA_DEVICES_COUNT, devicesCount);
        sendBroadcast(intent);
    }

    private void headsUpNotification() {
        Random random =new Random();
        int randomid = random.nextInt(100);
        int NOTIFICATION_ID = randomid;

        String iid = Integer.toString(NOTIFICATION_ID);
        Log.e ("Notification ID:",iid);


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_bluetooth)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.beacon))
                        .setContentTitle("Discovered Beacon")
                        .setContentText("If you visit website,Please click VIEW")
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
//    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://memseek.com"));
        Intent intent = new Intent(BackgroundService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
        //PendingIntent dismissIntent = PendingIntent.getActivity(this, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(R.drawable.view, "VIEW", pendingIntent);
        builder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
