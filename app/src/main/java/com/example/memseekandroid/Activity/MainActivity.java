package com.example.memseekandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.memseekandroid.R;
import com.example.memseekandroid.Utils.NotificationReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Button registationbt;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
        setbutton();
        event();
        startAt10();
    }
    private void setbutton(){
        registationbt=findViewById(R.id.registrationbtn);
    }
    private void event(){
        registationbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(MainActivity.this, "This device does not support the Bluetooth function.", Toast.LENGTH_SHORT).show();
                } else if (!mBluetoothAdapter.isEnabled()) {
                    // Bluetooth is not enabled :)
                    Intent i=new Intent(MainActivity.this,BluetoothActivity.class);
                    startActivity(i);
//                    startAt10();


                } else {
                    // Bluetooth is enabled
                    Intent I=new Intent(MainActivity.this,BackgroundActivity.class);
                    startActivity(I);

//                    startAt10();
                }
            }

        });
    }

    public void startAt10() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 60*12;
        /* Set the alarm to start at 13:00 PM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 11);
        /* Repeating on every 5 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
    }


}
