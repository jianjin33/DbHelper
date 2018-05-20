package com.jianjin33.dbhelper;

import com.jianjin33.dblib.BaseDao;

/**
 * Created by Administrator on 2018/5/20.
 */
public class UserDao extends BaseDao<User> {
    @Override
    protected String createTable() {
        return "CREATE TABLE if not exists user (user_name varchar(20),pwd varchar(20))";
    }
}
