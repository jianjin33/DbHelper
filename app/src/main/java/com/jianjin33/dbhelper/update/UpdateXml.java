package com.jianjin33.dbhelper.update;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 * 解析xml
 */
public class UpdateXml {

    private final List<CreateVersion> createVersions;

    public UpdateXml(Document document) {

        {
            /**
             * 获取升级的版本
             */
            NodeList createVersions = document.getElementsByTagName("createVersion");
            this.createVersions = new ArrayList<>();
            for (int i = 0; i < createVersions.getLength(); i++) {
                Element element = (Element) createVersions.item(i);
                CreateVersion cv = new CreateVersion(element);
                this.createVersions.add(cv);
            }
        }
    }


    public List<CreateVersion> getCreateVersions() {
        return createVersions;
    }
}
