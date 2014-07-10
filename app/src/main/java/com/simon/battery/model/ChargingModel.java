package com.simon.battery.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by simon on 09.07.14.
 */
@DatabaseTable(tableName = "charging")
public class ChargingModel {

    public ChargingModel() {
    }

    public ChargingModel(long id, String date, int chargingTime) {
        this.id = id;
        this.date = date;
        this.chargingTime = chargingTime;
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String date;
    @DatabaseField
    private int chargingTime;


}

