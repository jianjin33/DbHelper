package com.jianjin33.dbhelper.update;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/5/25.
 */

public class DbUpdateManager {

    private File parentFile;

    public DbUpdateManager(String fileName) {
        parentFile = new File(Environment.getExternalStorageDirectory(),fileName);

    }
}
