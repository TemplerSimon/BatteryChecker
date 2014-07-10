package com.simon.battery.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by simon on 09.07.14.
 */
@DatabaseTable(tableName = "discharging")
public class DisChargingModel {

    public static final String ID = "id";
    public static final String STARTDATE = "startdate";
    public static final String STOPDATE = "stopdate";
    public static final String STARTLEVEL = "startlevel";
    public static final String STOPLEVEL = "stoplevel";

    public DisChargingModel() {
    }

    public DisChargingModel(long id, String startDate, String stopDate,
                            int startlevel, int stoplevel) {
        this.id = id;
        this.startdate = startDate;
        this.stopdate = stopDate;
        this.startlevel = startlevel;
        this.stoplevel = stoplevel;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(defaultValue="0")
    private String startdate;
    @DatabaseField(defaultValue="0")
    private String stopdate;
    @DatabaseField
    private int startlevel;
    @DatabaseField
    private int stoplevel;

    public long getId() {
        return id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStopdate() {
        return stopdate;
    }

    public void setStopdate(String stopdate) {
        this.stopdate = stopdate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartlevel() {
        return startlevel;
    }

    public void setStartlevel(int startlevel) {
        this.startlevel = startlevel;
    }

    public int getStoplevel() {
        return stoplevel;
    }

    public void setStoplevel(int stoplevel) {
        this.stoplevel = stoplevel;
    }

}
