package com.simon.battery.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import com.simon.battery.model.BatteryModel;
import com.simon.battery.model.DisChargingModel;
import com.simon.battery.model.InternalConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 09.07.14.
 */
public class BatteryRepository {

    private static final long ONE_DAY = 86400000;
    private BatteryCheckerDaoHelper db;
    Dao<BatteryModel, Integer> batteryModelDao;
    Dao<DisChargingModel, Integer> disChargingModelDao;
    Dao<InternalConfiguration, Integer> internalConfDao;

    public BatteryRepository(Context ctx) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(ctx);
            batteryModelDao = db.getBatteryCheckerDao();
            disChargingModelDao = db.getDisChargingBatteryDao();
            internalConfDao = db.getInternalConfDao();
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }

    }

    public int create(BatteryModel batteryModel) {
        try {
            return batteryModelDao.create(batteryModel);
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int update(BatteryModel batteryModel) {
        try {
            return batteryModelDao.update(batteryModel);
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int updateDisCharging(DisChargingModel model) {
        try {
            return disChargingModelDao.update(model);
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(BatteryModel batteryModel) {
        try {
            return batteryModelDao.delete(batteryModel);
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public List<BatteryModel> getAll() {
        try {
            return batteryModelDao.queryForAll();
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<DisChargingModel> getAllDisCharging() {
        try {
            return disChargingModelDao.queryForAll();
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public BatteryModel findById(Integer id) {

        try {
            batteryModelDao.queryForId(id);
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public int deleteByDate(Long date) {
        List<BatteryModel> lista = new ArrayList<BatteryModel>();
        int count = 0;
        long writeDate;
        long id = 0;
        DeleteBuilder<BatteryModel, Integer> deleteBuilder = batteryModelDao
                .deleteBuilder();
        try {
            lista = batteryModelDao.queryForAll();

            for (BatteryModel model : lista) {
                writeDate = Long.parseLong(model.getDate());
                if ((date - writeDate) >= ONE_DAY) {
                    id = model.getId();
                    break;
                }
            }
            if (id != 0) {
                deleteBuilder.where().le("id", id);
                count = batteryModelDao.delete(deleteBuilder.prepare());
            }

        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public int createDisCharging(DisChargingModel model) {
        try {
            return disChargingModelDao.create(model);
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @return last model object from DisCharging table or null if SqlExeption or no object in DB
     */
    public DisChargingModel getLastObject() {
        try {
            return disChargingModelDao.queryForId((int) disChargingModelDao.countOf());
        } catch (java.sql.SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param currentValue - maxValue of the conf property
     * @param name         - name of the property
     * @author simon
     */
    public void setInternalConfValue(String name, int currentValue) {
        InternalConfiguration intConf = new InternalConfiguration();

        try {
            intConf = getFirstInternalConfiguration(name);

            if (intConf == null) {
                InternalConfiguration intConfNew = new InternalConfiguration();
                intConfNew.setName(name);
                intConfNew.setCurrentvalue(0);
                internalConfDao.create(intConfNew);
            } else if (intConf != null) {
                intConf.setName(name);
                intConf.setCurrentvalue(currentValue);
                internalConfDao.update(intConf);
            }
        } catch (SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
        }
    }

    /**
     * @param name
     * @return Internal configuration parameter obtained by name.
     * @author simon
     */
    public InternalConfiguration getLastInternalConfValue(String name) {
        InternalConfiguration intConf = new InternalConfiguration();

        intConf = getFirstInternalConfiguration(name);

        if (intConf == null) {
            return null;
        } else {
            return intConf;
        }
    }

    private InternalConfiguration getFirstInternalConfiguration(String name) {
        try {
            QueryBuilder<InternalConfiguration, Integer> queryBuilder = internalConfDao.queryBuilder();

            queryBuilder.where().eq(InternalConfiguration.NAME, name);

            PreparedQuery<InternalConfiguration> preparedQuery = queryBuilder.prepare();

            return internalConfDao.queryForFirst(preparedQuery);

        } catch (SQLException e) {
            Log.e(BatteryRepository.class.getName(), e.getMessage());
        }
        return null;
    }

}
