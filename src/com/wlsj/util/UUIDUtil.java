package com.wlsj.util;

import java.util.UUID;

public class UUIDUtil {
    public static final String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
