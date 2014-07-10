package com.simon.batterychecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.simon.discharging.DisChargingActivity;
import com.simon.preferences.BatteryCheckerPreferences;


public class BatteryChecker extends Activity {

    private static final String LABEL = "BatteryChecker";
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_checker);
    }

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

    public void startService(View v) {
        i = new Intent(this, BatteryCheckerService.class);
        this.startService(i);
    }

    public void stopService(View v) {
        i = new Intent(this, BatteryCheckerService.class);
        this.stopService(i);
    }

    public void restartService(View v) {
        Intent i = new Intent(this, BatteryCheckerService.class);
        this.stopService(i);
        this.startService(i);
    }

/*    public void makeChart(View v) {
        Intent i = new Intent(this, BatteryXYPlotActivity.class);
        startActivity(i);
    }*/

    public void showDischarging(View v) {
        Intent i = new Intent(this, DisChargingActivity.class);
        startActivity(i);
    }

}
