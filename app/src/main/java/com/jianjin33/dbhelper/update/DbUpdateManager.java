package com.jianjin33.dbhelper.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Administrator on 2018/5/25.
 *
 */
public class DbUpdateManager {

    private static final String TAG = DbUpdateManager.class.getSimpleName();
    private File parentFile;

    public DbUpdateManager(String fileName) {
        parentFile = new File(Environment.getExternalStorageDirectory(), fileName);

        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    public void checkThisVersionTable(Context context) {
        UpdateXml updateXml = readDbXml(context);

        String thisVersion = this.getVersionName(context);

        CreateVersion createVersion = analyseCreateVersion(thisVersion, updateXml);

        try {
            executeCreateVersion(createVersion, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeCreateVersion(CreateVersion createVersion, boolean b) throws Exception {
        if (createVersion == null || createVersion.getCreateDbs() == null) {
            throw new Exception("createVersion or createDbs is null;");
        }

        for (CreateDb cd : createVersion.getCreateDbs()) {
            if (cd == null || cd.getDbName() == null) {
                throw new Exception("db or dbName is null when createVersion;");
            }

            List<String> sqls = cd.getSqlCreates();
            SQLiteDatabase sqLiteDb = getDb(cd.getDbName());
            executeSql(sqLiteDb, sqls);
            sqLiteDb.close();
        }

    }

    private void executeSql(SQLiteDatabase sqLiteDb, List<String> sqls) {
        // 检查参数
        if (sqls == null || sqls.size() == 0) {
            return;
        }

        // 事务
        sqLiteDb.beginTransaction();
        for (String sql : sqls) {

            sql = sql.replaceAll("\r\n", " ");
            sql = sql.replaceAll("\n", " ");
            if (!"".equals(sql.trim())) {
                try {
                    Log.i(TAG, "执行sql：" + sql);
                    sqLiteDb.execSQL(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        sqLiteDb.setTransactionSuccessful();
        sqLiteDb.endTransaction();
    }

    private CreateVersion analyseCreateVersion(String thisVersion, UpdateXml updateXml) {
        if (thisVersion == null || updateXml == null) return null;
        CreateVersion cv = null;

        List<CreateVersion> createVersions = updateXml.getCreateVersions();
        if (createVersions != null) {
            for (CreateVersion item : createVersions) {
                // 如果表相同则要支持xml中逗号分隔
                String[] createVersion = item.getVersion().trim().split(",");
                for (int i = 0; i < createVersion.length; i++) {
                    if (createVersion[i].trim().equalsIgnoreCase(thisVersion)) {
                        cv = item;
                        break;
                    }
                }
            }
        }
        return cv;
    }

    private SQLiteDatabase getDb(CreateDb db) {
        return getDb(db.getDbName());
    }

    private UpdateXml readDbXml(Context context) {
        InputStream is = null;
        Document document = null;
        try {
            is = context.getAssets().open("updateXml.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        UpdateXml xml = new UpdateXml(document);
        return xml;
    }

    private SQLiteDatabase getDb(String dbName) {
        String dbfilepath = null;
        SQLiteDatabase sqlitedb = null;

        if (dbName.equalsIgnoreCase("user")) {
            dbfilepath = parentFile.getAbsolutePath() + "/user.db";// logic对应的数据库路径

        } else if (dbName.equalsIgnoreCase("xxx")) {
            dbfilepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/user.db";// service对应的数据库
        }

        if (dbfilepath != null) {
            File f = new File(dbfilepath);
            f.mkdirs();
            if (f.isDirectory()) {
                f.delete();
            }
            sqlitedb = SQLiteDatabase.openOrCreateDatabase(dbfilepath, null);
        }
        return sqlitedb;
    }

    /**
     * 获取APK版本号
     *
     * @param context 上下文
     * @return 版本号
     * @throws throws [违例类型] [违例说明]
     * @see
     */
    public String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
