package com.jianjin33.dbhelper.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class CreateVersion {

    private final List<CreateDb> createDbs;
    private final String version;

    public CreateVersion(Element element) {
        version = element.getAttribute("version");

        {
            createDbs = new ArrayList<>();
            NodeList cs = element.getElementsByTagName("createDb");
            for (int i = 0; i < cs.getLength(); i++) {
                Element ci = (Element) cs.item(i);
                CreateDb cd = new CreateDb(ci);
                this.createDbs.add(cd);
            }
        }
    }

    public List<CreateDb> getCreateDbs() {
        return createDbs;
    }

    public String getVersion() {
        return version;
    }
}
