package com.jianjin33.dbhelper.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class CreateDb {

    private final List<String> sqlCreates;
    private final String dbName;

    public CreateDb(Element ci) {

        dbName = ci.getAttribute("name");
        {
            sqlCreates = new ArrayList<>();
            NodeList sqls = ci.getElementsByTagName("sql_createTable");
            for (int i = 0; i < sqls.getLength(); i++) {
                String sqlCreate = sqls.item(i).getTextContent();
                this.sqlCreates.add(sqlCreate);
            }
        }
    }

    public List<String> getSqlCreates() {
        return sqlCreates;
    }

    public String getDbName() {
        return dbName;
    }
}
