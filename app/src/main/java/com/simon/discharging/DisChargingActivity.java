package com.simon.discharging;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.simon.battery.dao.BatteryRepository;
import com.simon.batterychecker.R;


/**
 * Created by simon on 09.07.14.
 */
public class DisChargingActivity extends Activity {

    private BatteryRepository repos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repos = new BatteryRepository(this);

        setContentView(R.layout.discharging);
        GridView gridview = (GridView) findViewById(R.id.discharging);

        gridview.setAdapter(new DataAdapter(this, repos.getAllDisCharging()));

    }

}
