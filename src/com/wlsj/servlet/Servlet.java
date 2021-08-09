package com.wlsj.servlet;

import com.wlsj.exception.base.ServletException;
import com.wlsj.request.Request;
import com.wlsj.response.Response;

import java.io.IOException;

public interface Servlet {
    void service(Request request, Response response) throws ServletException, IOException;

    void init();

    void destroy();
}
