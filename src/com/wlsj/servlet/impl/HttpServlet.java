package com.wlsj.servlet.impl;

import com.wlsj.enumeration.RequestMethod;
import com.wlsj.exception.base.ServletException;
import com.wlsj.request.Request;
import com.wlsj.response.Response;
import com.wlsj.servlet.Servlet;

import java.io.IOException;

public abstract class HttpServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws ServletException, IOException {
        if (request.getRequsetMethod() == RequestMethod.GET) {
            this.doGet(request, response);
        } else if (request.getRequsetMethod() == RequestMethod.POST) {
            this.doPost(request, response);
        } else if (request.getRequsetMethod() == RequestMethod.PUT) {
            this.doPut(request, response);
        } else if (request.getRequsetMethod() == RequestMethod.DELETE) {
            this.doDelete(request, response);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public void doGet(Request request, Response response) throws ServletException, IOException {

    }

    public void doPost(Request request, Response response) throws ServletException, IOException {

    }

    public void doPut(Request request, Response response) throws ServletException, IOException {

    }

    public void doDelete(Request request, Response response) throws ServletException, IOException {

    }


}
