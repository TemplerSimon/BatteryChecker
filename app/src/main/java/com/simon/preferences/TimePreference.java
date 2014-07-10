package com.simon.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by simon on 09.07.14.
 */
public class TimePreference extends DialogPreference {

    private static final String androidns = "http://schemas.android.com/apk/res/android";
    private int lastHourStart = 0;
    private int lastMinuteStart = 0;
    private int lastHourStop = 0;
    private int lastMinuteStop = 0;

    private TextView startTimeText, stopTimeText;
    private Context mContext;
    private String mDialogMessage, mDefault;
    private TimePicker pickerStart = null;
    private TimePicker pickerStop = null;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setDialogTitle("");

        mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        mDefault = attrs.getAttributeValue(androidns, "defaultValue");

    }

    @Override
    protected View onCreateDialogView() {

        ScrollView scrollView = new ScrollView(mContext);

        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(mContext);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);

        startTimeText = new TextView(mContext);
        if (mDialogMessage != null)
            startTimeText.setText(mDialogMessage);

        stopTimeText = new TextView(mContext);
        stopTimeText.setText("Stop time");

        scrollView.addView(layout);

        layout.addView(startTimeText);

        pickerStart = new TimePicker(mContext);

        layout.addView(pickerStart, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        layout.addView(stopTimeText);

        pickerStop = new TimePicker(mContext);

        layout.addView(pickerStop, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        pickerStart.setCurrentHour(22);
        pickerStart.setCurrentMinute(00);

        pickerStop.setCurrentHour(06);
        pickerStop.setCurrentMinute(00);

        return scrollView;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        pickerStart.setCurrentHour(lastHourStart);
        pickerStart.setCurrentMinute(lastMinuteStart);

        pickerStop.setCurrentHour(lastHourStop);
        pickerStop.setCurrentMinute(lastMinuteStop);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        String time;

        if (positiveResult) {
            lastHourStart = pickerStart.getCurrentHour();
            lastMinuteStart = pickerStart.getCurrentMinute();
            lastHourStop = pickerStop.getCurrentHour();
            lastMinuteStop = pickerStop.getCurrentMinute();

            String timeStart = String.valueOf(lastHourStart) + ":"
                    + String.valueOf(lastMinuteStart);

            String timeStop = String.valueOf(lastHourStop) + ":"
                    + String.valueOf(lastMinuteStop);

            time = timeStart + ";" + timeStop;

            if (callChangeListener(timeStart)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time = null;
        String[] interver;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("22:00;06:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        interver=time.split(";");

        lastHourStart = getHour(interver[0]);
        lastMinuteStart = getMinute(interver[0]);
        lastHourStop = getHour(interver[1]);
        lastMinuteStop = getMinute(interver[1]);
    }

    public static int getHour(String time) {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");
        return (Integer.parseInt(pieces[1]));
    }

}
