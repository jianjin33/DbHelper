package com.jianjin33.dbhelper;

import com.jianjin33.dblib.annotation.DbField;
import com.jianjin33.dblib.annotation.DbTable;

/**
 * Created by Administrator on 2018/5/20.
 */
@DbTable("db_file")
public class FileBean {

    @DbField("time")
    public String time;

    @DbField("path")
    public String path;

    @DbField("description")
    public String description;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
