package com.jianjin33.dbhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jianjin33.dblib.BaseDaoFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);

    }

    public void insert(View view) {
        User user = new User("张三", "123456");
        User user1 = new User("Json", "666555");
        userDao.insert(user);
        userDao.insert(user1);
    }

    public void update(View view) {
        User where = new User();
        where.setUserName("Json");

        User user = new User("李四", "555555");
        int result = userDao.update(user,where);
        Log.d(TAG,"result" + result);
    }

    public void delete(View view) {

    }

    public void query(View view) {

    }
}
