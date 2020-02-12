package com.example.memseekandroid.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memseekandroid.Adapter.RecyclerViewAdapter;
import com.example.memseekandroid.Dialog.LoadingDialog;
import com.example.memseekandroid.R;
import com.example.memseekandroid.Service.BackgroundService;
import com.example.memseekandroid.Utils.NotificationReceiver;
import com.kontakt.sdk.android.ble.device.BeaconDevice;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class BackgroundActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener{
    //    private PendingIntent pendingIntent;
    private Intent serviceIntent;
//    private TextView statusText;
    RecyclerViewAdapter adapter;
    LoadingDialog progressDialog;

    ArrayList<String>beaconUUID=new ArrayList<>();
    ArrayList<String>beaconMojor=new ArrayList<>();
    ArrayList<String>beaconMinor=new ArrayList<>();
    ArrayList<String>beaconDistance=new ArrayList<>();
    ArrayList<String>beaconRssi=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        initialize();
        startBackgroundService();
        // set up the RecyclerView
        RecyclerView recyclerView= findViewById(R.id.beaconview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackgroundActivity.this));
        adapter=new RecyclerViewAdapter(BackgroundActivity.this,beaconUUID,beaconMojor,beaconMinor,beaconDistance,beaconRssi);
        adapter.setClickListener(BackgroundActivity.this);
        recyclerView.setAdapter(adapter);

        progressDialog = new LoadingDialog(this, false);
        progressDialog.show();
    }
    private void initialize(){
        // Init views
//        statusText =findViewById(R.id.statusText);
        // Set service intent
        serviceIntent = BackgroundService.createIntent(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        // Register Broadcast receiver that will accept results from background scanning(discovered)
        IntentFilter intentFilter=new IntentFilter(BackgroundService.ACTION_DEVICE_DISCOVERED);
        registerReceiver(scanningBroadcastReceiver,intentFilter);
        //Register Broadcast receiver that will accept results from backgroud scannig(losted)
        IntentFilter intentFilter1=new IntentFilter(BackgroundService.ACTION_DEVICE_LOSTED);
        registerReceiver(scanningBroadcastReceiver1,intentFilter1);
    }
    @Override
    protected void onPause() {
        unregisterReceiver(scanningBroadcastReceiver);
        unregisterReceiver(scanningBroadcastReceiver1);
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startBackgroundService() {
        // please you have to FILE_LOCATION permisson over android 8.0
        //send permission request to Mainfest
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            startService(serviceIntent);
        }
    }
    //receive permission request from Mainfrest.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(serviceIntent);
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private final BroadcastReceiver scanningBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Device discovered!
//            int devicesCount = intent.getIntExtra(BackgroundService.EXTRA_DEVICES_COUNT, 0);
            RemoteBluetoothDevice device = intent.getParcelableExtra(BackgroundService.EXTRA_DEVICE);
            BeaconDevice iBeacondevice = (BeaconDevice)device;
//            statusText.setText(String.format("Total discovered devices: %d\n\nLast scanned device:\n%s", devicesCount, device.toString()));

            UUID uuid = iBeacondevice.getProximityUUID();
            String major = Integer.toString(iBeacondevice.getMajor());
            String minor = Integer.toString(iBeacondevice.getMinor());
            String distance=Double.toString(iBeacondevice.getDistance());
            String rssi=Integer.toString(iBeacondevice.getRssi());
            IteamData(intent,uuid.toString(),major,minor,distance,rssi);

            progressDialog.hide();
        }
    };

    private void IteamData(Intent intent,String uuid,String major,String minnor,String distance,String rssi){
            if (beaconUUID.contains(uuid) && beaconMojor.contains(major) && beaconMinor.contains(minnor)) {
                return;
            }
            beaconUUID.add(uuid);
            beaconMojor.add(major);
            beaconMinor.add(minnor);
            beaconDistance.add(distance);
            beaconRssi.add(rssi);
            adapter.notifyDataSetChanged();


    }





    private final BroadcastReceiver scanningBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Device discovered!
//            int devicesCount = intent.getIntExtra(BackgroundService.EXTRA_DEVICES_COUNT, 0);
            RemoteBluetoothDevice device = intent.getParcelableExtra(BackgroundService.EXTRA_DEVICE);
            BeaconDevice iBeacondevice = (BeaconDevice)device;
//            statusText.setText(String.format("Total discovered devices: %d\n\nLast scanned device:\n%s", devicesCount, device.toString()));

            UUID uuid = iBeacondevice.getProximityUUID();
            String major = Integer.toString(iBeacondevice.getMajor());
            String minor = Integer.toString(iBeacondevice.getMinor());
            String distance=Double.toString(iBeacondevice.getDistance());
            String rssi=Integer.toString(iBeacondevice.getRssi());
            IteamData1(intent,uuid.toString(),major,minor,distance,rssi);

//            progressDialog.hide();
        }
    };

    private void IteamData1(Intent intent,String uuid,String major,String minnor,String distance,String rssi){
        if (beaconUUID.contains(uuid) && beaconMojor.contains(major) && beaconMinor.contains(minnor)) {
            beaconUUID.remove(uuid);
            beaconMojor.remove(major);
            beaconMinor.remove(minnor);
            beaconDistance.remove(distance);
            beaconRssi.remove(rssi);
            adapter.notifyDataSetChanged();
        }

            }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://memseek.com"));
        startActivity(browserIntent);
        //navigation.openWebViewsActivity(device);

    }

}
