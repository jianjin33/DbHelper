package com.jianjin33.dbhelper;

import com.jianjin33.dblib.BaseDao;

/**
 * Created by Administrator on 2018/5/20.
 */
public class FileDao extends BaseDao {
    @Override
    protected String createTable() {
        return "CREATE TABLE if not exists tb_file (time varchar(20)," +
                "path varchar(10),description varchar(20))";
    }
}
