package com.example.simplefirebasenotification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    //CPU vairiables
    TextView textViewCPU ;
    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        setTitle("Notification Metrics");

        //get views by id
        final TextView ChargeCycles = (TextView) findViewById(R.id.chargeCycles);
        final TextView BatteryIsCharging = (TextView) findViewById(R.id.batteryCharging);
        final TextView pluggedIn = (TextView) findViewById(R.id.pluggedIn);
        final TextView wirelessCharging = (TextView) findViewById(R.id.wirelessCharging);


        //Get Device State
        // Charging State
        Context context = this;
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        //Battery Cycles
        int chargeCount = BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER;

        //test metrics
        //ActivityManager.MemoryInfo.

        //Display Metrics
        //Charge Cycles
        ChargeCycles.setText(Integer.toString(chargeCount));

        //Is phone charging
        if (isCharging) {
            BatteryIsCharging.setText("True");
        }else{
            BatteryIsCharging.setText("False");
        }
        //Is device plugged in
        if (usbCharge) {
            pluggedIn.setText("True");
        }else{
            pluggedIn.setText("False");
        }
        //is device Wireless charging
        if (acCharge) {
            wirelessCharging.setText("True");
        }else{
            wirelessCharging.setText("False");
        }

        //CPU

        textViewCPU = (TextView)findViewById(R.id.textViewCPU);

        byteArry = new byte[1024];
        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){

                Holder = Holder + new String(byteArry);
            }
            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }
        textViewCPU.setText(Holder);

        //Storage
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
        long bytesAvailable = stat.getFreeBytes();
        long megAvailable = bytesAvailable / 1048576;
        long bytesTotal = stat.getTotalBytes();
        long megTotal = bytesTotal / 1048576;
        final TextView textViewStorage = (TextView)findViewById(R.id.storage);
        textViewStorage.setText(Float.toString(megTotal) + "/" + Float.toString(megAvailable) + " MB");


        //RAM
        ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem / 1048576;;
        long availRAM = memInfo.availMem / 1048576;;
        boolean lowMemory = memInfo.lowMemory;
        final TextView textViewRAM = (TextView)findViewById(R.id.ram);
        final TextView textViewLowRAM = (TextView)findViewById(R.id.lowMemory);
        textViewRAM.setText(Float.toString(totalMemory) +"/"+ Float.toString(availRAM)+ " MB");
        if (lowMemory){
            textViewLowRAM.setText("TRUE");
        } else{
            textViewLowRAM.setText("FALSE");
        }

    }


}
