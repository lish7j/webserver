package com.wlsj.servlet.impl;

import com.wlsj.exception.base.ServletException;
import com.wlsj.request.Request;
import com.wlsj.response.Response;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) throws ServletException, IOException {
        //if (request.getParameter("username").equalsIgnoreCase("hello")) {
            String content = "<html><head><title>lkk</title></head><body>hello</body></html>";
            response.setBody(content.getBytes("utf-8"));

        //}
    }

    @Override
    public void doPost(Request request, Response response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    public void doPut(Request request, Response response) throws ServletException, IOException {
        super.doPut(request, response);
    }

    @Override
    public void doDelete(Request request, Response response) throws ServletException, IOException {
        super.doDelete(request, response);
    }
}
