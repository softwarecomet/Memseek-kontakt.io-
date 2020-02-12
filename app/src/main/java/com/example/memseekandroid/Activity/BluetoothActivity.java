package com.example.memseekandroid.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.memseekandroid.R;


public class BluetoothActivity extends AppCompatActivity {

    final BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
    Button bton,btoff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        setbutton();
        buttonevent();

    }
    private void setbutton(){
        bton = findViewById(R.id.bluetoothon);
        btoff =findViewById(R.id.bluetoothoff);
    }
    private void buttonevent(){
        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blueAdapter == null)
                {
                    Toast.makeText(getApplicationContext(),"Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!blueAdapter.isEnabled()){
                        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);
                    }
                }
            }
        });

        btoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueAdapter.disable();
                Toast.makeText(getApplicationContext(),"Bluetooth Turned OFF", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Go to next screen
                Intent i=new Intent(BluetoothActivity.this, BackgroundActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth Denyed", Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
        }
    }
}
