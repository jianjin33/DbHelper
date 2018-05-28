package com.jianjin33.dbhelper;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.jianjin33.dbhelper.update.DbUpdateManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testUpdateManager(){
        DbUpdateManager dbUpdateManager = new DbUpdateManager("testfile");
        Context context = getContext();
        dbUpdateManager.checkThisVersionTable(context);
    }

}