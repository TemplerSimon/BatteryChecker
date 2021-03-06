package com.simon.batterychecker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.simon.battery.dao.BatteryRepository;
import com.simon.battery.model.BatteryModel;
import com.simon.battery.model.DisChargingModel;
import com.simon.battery.model.InternalConfiguration;

import java.util.Calendar;
import java.util.List;

/**
 * Created by simon on 09.07.14.
 */
public class BatteryCheckerService extends Service {


    Button startButton;
    Button stopButton;

    private static final String ALARM_COUNT = "countLowLevel";

    private NotificationManager mNM;
    private static final String SPLIT = ";";
    private static final int HOUR = 60;
    private int STATE_NOTIFICATION = R.string.battery_state;
    private int FULL_NOTIFICATION = R.string.battery_state_full;
    private Uri alarmSound;
    private Uri lowLevelSound;
    private Uri fullLevelSound;
    private Uri noneSound;

    private Calendar cal;

    private static final String LABEL = "BatteryCheckerService";

    private int minLevel;
    private int maxLevel;
    private int status;
    private int lastLevel;
    private int lowLevelAlarmCount;

    private Integer startHour;
    private Integer startMinut;
    private Integer stopHour;
    private Integer stopMinut;

    private SharedPreferences sharedPrefs;

    // Database
    private BatteryRepository repos;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        lowLevelSound = Uri.parse(sharedPrefs
                .getString("LowLevelSound", "none"));
        fullLevelSound = Uri.parse(sharedPrefs.getString("FullLevelSound",
                "none"));

        if (sharedPrefs.getString("LowLevelSound", "none").equals("none")) {
            lowLevelSound = alarmSound;
        }
        if (sharedPrefs.getString("FullLevelSound", "none").equals("none")) {
            fullLevelSound = alarmSound;
        }
        noneSound = Uri.EMPTY;
        cal = Calendar.getInstance();

        // DB acces object
        repos = new BatteryRepository(this);

        lastLevel = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        getPreferences();

        try {
            Log.i(LABEL, "Received start id " + startId + ": " + intent);

            this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(
                    Intent.ACTION_BATTERY_CHANGED));

            this.registerReceiver(this.chargingInfoReceiver, new IntentFilter(
                    Intent.ACTION_POWER_CONNECTED));
            this.registerReceiver(this.chargingInfoReceiver, new IntentFilter(
                    Intent.ACTION_POWER_DISCONNECTED));

            this.registerReceiver(this.disChargingReceiver, new IntentFilter(
                    Intent.ACTION_POWER_CONNECTED));
            this.registerReceiver(this.disChargingReceiver, new IntentFilter(
                    Intent.ACTION_POWER_DISCONNECTED));

            this.registerReceiver(this.disChargingReceiver, new IntentFilter(
                    Intent.ACTION_BATTERY_CHANGED));
        } catch (IllegalArgumentException iae) {
            Log.e(LABEL, iae.getMessage());
        } catch (IllegalStateException ise) {
            Log.e(LABEL, ise.getMessage());
        }


        if (repos.getLastInternalConfValue(ALARM_COUNT) == null)
            repos.setInternalConfValue(ALARM_COUNT, 0);

        // LogToFile.configure();

        // log.info("Urchomienie uslugi");
        Toast.makeText(this, R.string.service_start, Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(batteryInfoReceiver);
        this.unregisterReceiver(chargingInfoReceiver);
        this.unregisterReceiver(disChargingReceiver);

        Toast.makeText(this, R.string.service_stop, Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("NewApi")
    private void showBatteryState(boolean makeSound) {

        // log.info("(showBatteryState) makeSound = " + makeSound);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.battery_state))
                .setContentText(getText(R.string.battery_state))
                .setSmallIcon(R.drawable.battery_info_icon_09)
                .setSound(makeSound ? lowLevelSound : noneSound)
                .setAutoCancel(true).build();

        mNM.notify(STATE_NOTIFICATION, noti);

    }

    @SuppressLint("NewApi")
    private void fullBatteryState(boolean makeSound) {

        // log.info("(fullBatteryState) makeSound = " + makeSound);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.battery_state_full))
                .setContentText(getText(R.string.battery_state_full))
                .setSmallIcon(R.drawable.battery_info_icon_01)
                .setSound(makeSound ? fullLevelSound : noneSound).build();

        mNM.notify(FULL_NOTIFICATION, noti);

    }

    private void getPreferences() {
        String levelMin;
        String levelMax;
        String silentTime;
        String lowLAC;
        String[] intervel;

        levelMin = sharedPrefs.getString("batteryMinLevel", "30");
        levelMax = sharedPrefs.getString("batteryMaxLevel", "100");
        silentTime = sharedPrefs.getString("startTime", "22:00;06:00");
        lowLAC = sharedPrefs.getString(ALARM_COUNT, "5");
        intervel = silentTime.split(SPLIT);

        try {
            minLevel = Integer.parseInt(levelMin);
            maxLevel = Integer.parseInt(levelMax);

            startHour = Integer.parseInt(intervel[0].split(":")[0]);
            startMinut = Integer.parseInt(intervel[0].split(":")[1]);

            stopHour = Integer.parseInt(intervel[1].split(":")[0]);
            stopMinut = Integer.parseInt(intervel[1].split(":")[1]);

            lowLevelAlarmCount = Integer.parseInt(lowLAC);

        } catch (NumberFormatException nfe) {
            minLevel = 30;
            maxLevel = 100;

            startHour = 22;
            startMinut = 00;

            stopHour = 06;
            stopMinut = 00;

            lowLevelAlarmCount = 5;

            Log.e(LABEL, nfe.getMessage());
        }
    }

    /**
     * Receiver listen to event when power source is plugged.
     */
    private BroadcastReceiver chargingInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //"Clean datebase from old values."
            new CleanDB().start();
            //Set ALARM_COUNT parameter to zero
            repos.setInternalConfValue(ALARM_COUNT, 0);
            mNM.cancel(STATE_NOTIFICATION);
            int charging = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        }
    };

    /**
     * Receiver for connecting and disconnecting power source
     */
    private BroadcastReceiver disChargingReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DisChargingModel model = new DisChargingModel();
            DisChargingModel modelLast = new DisChargingModel();

            List<DisChargingModel> all = repos.getAllDisCharging();

            modelLast = repos.getLastObject();
            String actualDate = System.currentTimeMillis() + "";

            if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
                mNM.cancel(FULL_NOTIFICATION);

            if (modelLast == null
                    && intent.getAction().equals(
                    Intent.ACTION_POWER_DISCONNECTED)) {
                model.setStartdate(actualDate);
                repos.createDisCharging(model);
            } else if (modelLast != null && modelLast.getStopdate().equals("0")
                    && intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                modelLast.setStopdate(actualDate);
                repos.updateDisCharging(modelLast);
            } else if (modelLast != null
                    && !modelLast.getStopdate().equals("0")
                    && intent.getAction().equals(
                    Intent.ACTION_POWER_DISCONNECTED)) {
                model.setStartdate(actualDate);
                repos.createDisCharging(model);

            }
        }
    };

    /**
     * Battery info receiver.
     */
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            BatteryModel model = new BatteryModel();
            InternalConfiguration alarmCountPref = new InternalConfiguration();

            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            alarmCountPref = repos.getLastInternalConfValue(ALARM_COUNT);

            if (lastLevel != level || lastLevel == 0) {
                if (level <= minLevel
                        && (status == BatteryManager.BATTERY_STATUS_DISCHARGING
                        || status == BatteryManager.BATTERY_STATUS_UNKNOWN || status == BatteryManager.BATTERY_STATUS_NOT_CHARGING)) {

                    if (alarmCountPref != null && alarmCountPref.getCurrentvalue() <= lowLevelAlarmCount) {
                        showBatteryState(makeSoundChecker());
                        repos.setInternalConfValue(ALARM_COUNT, alarmCountPref.getCurrentvalue() + 1);
                    }
                    if (alarmCountPref != null && alarmCountPref.getCurrentvalue() > lowLevelAlarmCount) {
                        showBatteryState(false);
                        repos.setInternalConfValue(ALARM_COUNT, alarmCountPref.getCurrentvalue() + 1);
                    } else if (alarmCountPref == null) {
                        showBatteryState(makeSoundChecker());
                        repos.setInternalConfValue(ALARM_COUNT, 1);
                    }

                } else if (level == maxLevel
                        && maxLevel != intent.getIntExtra(
                        BatteryManager.EXTRA_SCALE, 0)
                        && status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    fullBatteryState(makeSoundChecker());

                } else if (maxLevel == intent.getIntExtra(
                        BatteryManager.EXTRA_SCALE, 0)
                        && level == maxLevel
                        && status == BatteryManager.BATTERY_STATUS_FULL) {
                    fullBatteryState(makeSoundChecker());
                }

                lastLevel = level;
                model.setDate(System.currentTimeMillis() + "");
                model.setLevel(level);
                repos.create(model);
            }
        }

    };

    /**
     * Clean database from old data
     */
    private class CleanDB extends Thread {
        @Override
        public void run() {
            cleanDB();
        }
    }


    private void cleanDB() {
        int count;
        count = repos.deleteByDate(System.currentTimeMillis());
        // log.info("Usunieto " + count + " rekordow");
    }

    /**
     * Check that sound should by played.
     *
     * @return - true if sound should by played or false if sound shouldn't be played.
     */
    private boolean makeSoundChecker() {
        int currentHourMinut = cal.get(Calendar.HOUR_OF_DAY) * HOUR
                + cal.get(Calendar.MINUTE);
        int startHourMinut = (startHour * HOUR + startMinut);
        int stopHourMinut = (stopHour * HOUR + stopMinut);

        if (startHourMinut > stopHourMinut) {
            return !(currentHourMinut <= stopHourMinut
                    || currentHourMinut >= startHourMinut);
        } else
            return startHourMinut >= stopHourMinut || !(currentHourMinut >= startHourMinut && currentHourMinut <= stopHourMinut);
    }

}
