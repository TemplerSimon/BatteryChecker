package com.simon.battery.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by simon on 17.07.14.
 */
@DatabaseTable(tableName = "internalconfiguration")
public class InternalConfiguration {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CURRENTVALUE = "currentvalue";

    public InternalConfiguration() {

    }

    public InternalConfiguration(long id, String name, int currentvalue) {
        this.id = id;
        this.name = name;
        this.currentvalue = currentvalue;
    }


    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String name;
    @DatabaseField(defaultValue = "0")
    private int currentvalue;
    @DatabaseField(defaultValue = "0")
    private int maxvalue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentvalue() {
        return currentvalue;
    }

    public void setCurrentvalue(int currentvalue) {
        this.currentvalue = currentvalue;
    }

}
