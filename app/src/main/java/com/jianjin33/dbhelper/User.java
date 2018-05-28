package com.jianjin33.dbhelper;

import com.jianjin33.dblib.annotation.DbField;
import com.jianjin33.dblib.annotation.DbTable;

/**
 * Created by Administrator on 2018/5/20.
 * test
 */
@DbTable("tb_user")
public class User {

    @DbField("user_name")
    private String userName;

    @DbField("pwd")
    private String password;

    @DbField("age")
    private int age;

    @DbField("state")
    private int state;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
