package com.wlsj;

import com.wlsj.network.server.bio.BioServer;
import com.wlsj.util.PropertiesUtil;

import java.util.Scanner;

public class BootStrap {

    /**
     * 服务器启动入口
     * 用户程序与服务器的接口
     */
    public static void main(String[] args) {
        String port = PropertiesUtil.getProperty("server.port");
        if(port == null) {
            throw new IllegalArgumentException("server.port 不存在");
        }
//        String connector = PropertiesUtil.getProperty("server.connector");
//        if(connector == null || (!connector.equalsIgnoreCase("bio") && !connector.equalsIgnoreCase("nio") && !connector.equalsIgnoreCase("aio"))) {
//            throw new IllegalArgumentException("server.network 不存在或不符合规范");
//        }

        int _port = Integer.parseInt(port);
        BioServer server = new BioServer(_port);
        server.start(_port);
//        Scanner scanner = new Scanner(System.in);
//
//        String order;
//        while (scanner.hasNext()) {
//            order = scanner.next();
//            if (order.equals("EXIT")) {
//                server.close();
//                System.exit(0);
//            }
//        }
    }
}
