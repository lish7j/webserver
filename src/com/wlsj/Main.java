package com.wlsj;

public class Main {
    public static void main(String[] args) {
        String path = Main.class.getResource("/com/wlsj/server.properties").getPath();
        System.out.println(path);
    }
}
