package com.simon.batterychecker;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.simon.discharging.DisChargingActivity;
import com.simon.preferences.BatteryCheckerPreferences;


public class BatteryChecker extends Activity {

    private static final String LABEL = "BatteryChecker";
    private Intent i;


    Button startButton;
    Button stopButton;

    /*
    Create the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_checker);


        startButton = (Button) findViewById(R.id.button1);
        stopButton = (Button) findViewById(R.id.button2);

        isMyServiceRunning(BatteryCheckerService.class);
    }

    /*

     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.battery_checker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Launch settings activity
                Intent i = new Intent(this, BatteryCheckerPreferences.class);
                startActivity(i);
                break;
        }
        return true;
    }

    /*
    Start service
     */
    public void startService(View v) {
        i = new Intent(this, BatteryCheckerService.class);
        this.startService(i);
        startButton.setBackgroundColor(Color.GREEN);
    }

    /*
    Stop service
     */
    public void stopService(View v) {
        i = new Intent(this, BatteryCheckerService.class);
        this.stopService(i);
        startButton.setBackgroundColor(Color.RED);
    }

    /*
    Restart service
     */
    public void restartService(View v) {
        Intent i = new Intent(this, BatteryCheckerService.class);
        this.stopService(i);
        this.startService(i);


    }

/*    public void makeChart(View v) {
        Intent i = new Intent(this, BatteryXYPlotActivity.class);
        startActivity(i);
    }*/


    /*
    Show statistics about battery discharging.
     */
    public void showDischarging(View v) {
        Intent i = new Intent(this, DisChargingActivity.class);
        startActivity(i);
    }

    /*
    Check that service is running and change color of start or stop button.
     */
    private void isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                startButton.setBackgroundColor(Color.GREEN);
            }
        }
        startButton.setBackgroundColor(Color.RED);
    }
}

