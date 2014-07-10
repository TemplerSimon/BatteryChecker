package com.simon.battery.dao;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by simon on 09.07.14.
 */
public class DatabaseManager {


    private BatteryCheckerDaoHelper databaseHelper = null;

    // gets a helper once one is created ensures it doesnt create a new one
    public BatteryCheckerDaoHelper getHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context,
                    BatteryCheckerDaoHelper.class);
        }
        return databaseHelper;
    }

    //releases the helper once usages has ended
    public void releaseHelper(BatteryCheckerDaoHelper helper)
    {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }


}
