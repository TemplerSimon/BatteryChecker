package com.simon.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.simon.batterychecker.R;

/**
 * Created by simon on 09.07.14.
 */
public class BatteryCheckerPreferences extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
