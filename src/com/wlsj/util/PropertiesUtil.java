package com.wlsj.util;




import com.wlsj.BootStrap;

import java.io.*;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties;

    static {
        loadProperties();
    }

    private synchronized static void loadProperties() {
        properties = new Properties();
        InputStream in = null;
        try {
            in = BootStrap.class.getResourceAsStream("/com/wlsj/server.properties");
            //in = new FileInputStream(new File("D:/m.properties"));
            properties.load(in);
        } catch (FileNotFoundException e) {
            System.err.println("rpc.properties文件未找到");
        } catch (IOException e) {
            System.out.println("出现IOException");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println("properties文件流关闭异常");
                }

            }
        }
    }

    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        return (String)properties.getOrDefault(key, defaultValue);
    }
}
