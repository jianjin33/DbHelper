package com.jianjin33.dblib;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jianjin33.dblib.annotation.DbField;
import com.jianjin33.dblib.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/20.
 * 增删改查的实现
 */
public abstract class BaseDao<T> implements IBaseDao<T> {

    private SQLiteDatabase database;
    private boolean isInit;
    private Class<T> entityClass;
    private String tableName;
    private Map<String, Field> cacheMap;


    synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.database = sqLiteDatabase;
            this.entityClass = entity;
            DbTable dbTableAnnotation = entity.getAnnotation(DbTable.class);
            if (dbTableAnnotation == null) {
                tableName = entity.getSimpleName();
            } else {
                tableName = entity.getAnnotation(DbTable.class).value();
            }

            // 判断是否打开数据库
            if (!database.isOpen()) {
                return false;
            }
            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }

            initCacheMap();

            isInit = true;
        }
        return isInit;
    }

    /**
     * 实例化映射关系
     */
    private void initCacheMap() {
        cacheMap = new HashMap<>();
        String sql = "SELECT * FROM " + this.tableName + " limit 1 ,0";
        Cursor cursor = null;
        try {

            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();

            Field[] columnsFields = entityClass.getDeclaredFields();

            for (Field field : columnsFields) {
                field.setAccessible(true);
            }

            // 开始找对应关系
            for (String columnName : columnNames) {
                Field columnField = null;
                for (Field field : columnsFields) {
                    String fieldName = null;
                    DbField dbFieldAnnotation = field.getAnnotation(DbField.class);
                    if (dbFieldAnnotation == null) {
                        // 没有注解使用成员变量名
                        fieldName = field.getName();
                    } else {
                        fieldName = dbFieldAnnotation.value();
                    }

                    if (columnName.equals(fieldName)) {
                        columnField = field;
                        break;
                    }
                }

                if (columnField != null) {
                    cacheMap.put(columnName, columnField);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public Long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        Long result = database.insert(tableName, null, values);
        return result;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String values = map.get(key);
            if (values != null) {
                contentValues.put(key, values);
            }
        }
        return contentValues;
    }

    /**
     * @param entity
     * @return key为数据库column value为存入的值
     */
    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> iterator = cacheMap.values().iterator();
        while (iterator.hasNext()) {
            Field columnField = iterator.next();
            String cacheKey;
            String cacheValue = null;
            if (columnField.getAnnotation(DbField.class) != null) {
                cacheKey = columnField.getAnnotation(DbField.class).value();
            } else {
                cacheKey = columnField.getName();
            }

            try {
                // 反射拿到entity成员变量值
                if (columnField.get(entity) == null) {
                    continue;
                }
                cacheValue = columnField.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }

        return result;
    }

    @Override
    public int update(T entity, T where) {
        Map<String, String> values = getValues(entity);
        Map whereClause = getValues(where);

        Condition condition = new Condition(whereClause);
        ContentValues contentValues = getContentValues(values);

        int result = database.update(tableName, contentValues,
                condition.getWhereClause(), condition.getWhereArgs());

        return result;
    }

    @Override
    public Long delete(T where) {
        return null;
    }

    @Override
    public List<T> query(T where) {
        return null;
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        return null;
    }

    protected abstract String createTable();

    class Condition {
        /**
         * 查询条件
         * name=？ && pwd=?
         * 将传入的map解析成类似 db.update("tb_name",values,"1=1 and name=?",
         * new String[]{String.valueOf(user.getName}
         */
        private String whereClause;

        private String[] whereArgs;

        public Condition(Map<String, String> whereClause) {
            List<String> list = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("1=1"); // 为了方便使用and 前面加1=1防止报错
            Set keys = whereClause.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = whereClause.get(key);

                if (value != null) {
                    stringBuilder.append(" and " + key + "=?");
                    list.add(value);
                }
            }
            this.whereClause = stringBuilder.toString();
            this.whereArgs = list.toArray(new String[list.size()]);
        }

        public String getWhereClause() {
            return whereClause;
        }

        public void setWhereClause(String whereClause) {
            this.whereClause = whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }

        public void setWhereArgs(String[] whereArgs) {
            this.whereArgs = whereArgs;
        }
    }


}
