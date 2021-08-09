package com.wlsj.exception.handler;

import com.wlsj.exception.RequestInvalidException;
import com.wlsj.exception.base.ServletException;
import com.wlsj.network.wrapper.SocketWrapper;
import com.wlsj.response.Header;
import com.wlsj.response.Response;
import com.wlsj.util.IOUtil;

import java.io.IOException;

import static com.wlsj.constant.ContextConstant.ERROR_PAGE;



public class ExceptionHandler {
    public void handle(ServletException e, Response response, SocketWrapper socketWrapper) {
        try {
            if (e instanceof RequestInvalidException) {
                e.printStackTrace();
                System.out.println("ExceptionHandler 请求无法读取，丢弃");
                socketWrapper.close();
            } else {
                System.out.println("ExceptionHandler 抛出异常" + e.getClass().getName());
                e.printStackTrace();
                response.addHeader(new Header("Connection", "close"));
                response.setStatus(e.getStatus());
                response.setBody(IOUtil.getBytesFromFile(String.format(ERROR_PAGE, e.getStatus().getCode())));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
