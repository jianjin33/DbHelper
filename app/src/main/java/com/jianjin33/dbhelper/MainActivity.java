package com.jianjin33.dbhelper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jianjin33.dblib.BaseDaoFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {  //如果用户赋予权限，则调用相机
            userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
        } else {
            //未赋予权限，则做出对应提示
        }
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
        int result = userDao.update(user, where);
        Log.d(TAG, "result:update" + result);
    }

    public void delete(View view) {
        User where = new User();
        where.setUserName("Json");

        int result = userDao.delete(where);
        Log.d(TAG, "result:delete" + result);
    }

    public void query(View view) {
        User where = new User();
        where.setUserName("李四");

        List<User> list = userDao.query(where);
        if (list == null) return;

        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "result:query" + list.get(i).getUserName() + list.get(i).getPassword());
        }

    }
}
