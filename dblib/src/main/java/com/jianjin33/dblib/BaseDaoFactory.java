package com.jianjin33.dblib;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by Administrator on 2018/5/20.
 */
public class BaseDaoFactory {

    private String sqlDbPath;
    private SQLiteDatabase sqLiteDatabase;
    private static BaseDaoFactory factory;

    private BaseDaoFactory() {
        sqlDbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/user.db";
        openDb();
    }

    public static BaseDaoFactory getInstance(){
        if (factory == null){
            factory = new BaseDaoFactory();
        }
        return factory;
    }

    private void openDb() {
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqlDbPath,null);
    }

    public synchronized <T extends BaseDao<M>,M> T getDataHelper(Class<T> clazz,Class<M> entityClazz){
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClazz,sqLiteDatabase);


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
