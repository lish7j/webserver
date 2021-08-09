package com.wlsj.servlet.impl;

import com.wlsj.enumeration.RequestMethod;
import com.wlsj.exception.base.ServletException;
import com.wlsj.request.Request;
import com.wlsj.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DefaultServlet extends HttpServlet {
    @Override
    public void service(Request request, Response response) throws ServletException, IOException {
        if (request.getRequsetMethod() == RequestMethod.GET) {
            if (request.getUrl().equals("/")) {
                request.setUrl("/index.html");
            }
            request.getRequestDispatcher(request.getUrl()).forward(request, response);
        }

    }
}
