package com.wlsj.network.server;

import org.springframework.util.StringUtils;

public abstract class Server {

    public abstract void start(int port);

    public abstract void close();

    public static Server getInstance(String connector) {
        StringBuilder sb = new StringBuilder();
        sb.append("com.wlsj.network.server").append(".").append(connector)
                .append(".").append(StringUtils.capitalize(connector)).append("EndPoint");
        try {
            return (Server)Class.forName(sb.toString()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(connector);
    }

}