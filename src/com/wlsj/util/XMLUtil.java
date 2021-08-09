package com.wlsj.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class XMLUtil {
    public static Document getDocument(InputStream in) {
        try {
            SAXReader saxReader = new SAXReader();
            return saxReader.read(in);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
