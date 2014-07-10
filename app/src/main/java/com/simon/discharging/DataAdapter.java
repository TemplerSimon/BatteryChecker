package com.simon.discharging;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simon.battery.model.DisChargingModel;
import com.simon.batterychecker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by simon on 09.07.14.
 */
public class DataAdapter extends BaseAdapter {
    private Context mContext;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd kk:mm:ss");

    private List<DisChargingModel> localList;

    public DataAdapter(Context c, List<DisChargingModel> modelList) {
        this.mContext = c;
        this.localList = modelList;
    }

    @Override
    public int getCount() {
        return localList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Long startString;
        Long stopString;
        String disChargePercent = new String();

        View gridView;
        if (convertView == null) {
            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.gridrow, null);
            // set value into textview
            TextView startDate = (TextView) gridView
                    .findViewById(R.id.startDate);
            TextView stopDate = (TextView) gridView.findViewById(R.id.stopDate);
            TextView time = (TextView) gridView.findViewById(R.id.time);

            startString = Long
                    .parseLong(localList.get(position).getStartdate());
            stopString = Long.parseLong(localList.get(position).getStopdate());

            try {
                disChargePercent = localList.get(position).getStoplevel()
                        - localList.get(position).getStartlevel() + "";
            } catch (NumberFormatException nfe) {

                startString = 0L;
                stopString = 0L;

                Log.e(DataAdapter.class.toString(), nfe.getMessage());
                disChargePercent = null;
            }

            startDate.setText(startString == 0L ? "" : sdf.format(new Date(
                    startString)) + " ");
            stopDate.setText(stopString == 0L ? "" : sdf.format(new Date(
                    stopString)) + " ");
            time.setText(disChargePercent == null ? "" : disChargePercent + " ");

        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }
}
