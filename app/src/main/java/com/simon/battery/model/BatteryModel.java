package com.simon.battery.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by simon on 09.07.14.
 */
@DatabaseTable(tableName = "battery")
public class BatteryModel {

    public BatteryModel() {
    }

    public BatteryModel(long id, String date, int level) {
        this.id = id;
        this.date = date;
        this.level = level;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String date;
    @DatabaseField
    private int level;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}

