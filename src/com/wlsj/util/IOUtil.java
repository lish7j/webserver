package com.wlsj.util;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.*;

import java.io.*;

@Slf4j
public class IOUtil {
    public static byte[] getBytesFromFile(String fileName) throws IOException {
        InputStream in = IOUtil.class.getResourceAsStream(fileName);
        if (in == null) {
            System.out.println("Not Found File:{}" + fileName);
            throw new FileNotFoundException();
        }
        System.out.println("IOUtil 正在读取文件: " + fileName);
        return getBytesFromStream(in);
    }

    public static byte[] getBytesFromStream(InputStream in) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[]  buffer = new byte[1024];

        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byteArrayOutputStream.close();
        in.close();;
        return byteArrayOutputStream.toByteArray();

    }


}
