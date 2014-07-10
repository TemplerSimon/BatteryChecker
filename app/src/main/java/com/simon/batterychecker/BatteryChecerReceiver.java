package com.simon.batterychecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by simon on 09.07.14.
 */
public class BatteryChecerReceiver extends BroadcastReceiver {

    private static final String LABEL = "BatteryChecerReceiver";

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i(LABEL, "Enter BatteryChecerReceiver");
        try {
            Intent service = new Intent(context, BatteryCheckerService.class);
            context.startService(service);
            Log.i(LABEL, "Exit BatteryChecerReceiver");
        } catch (SecurityException se) {
            Log.e(LABEL, se.getMessage());
        }

    }

}
