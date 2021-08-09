package com.wlsj.request.dispatcher;

import com.wlsj.exception.base.ServletException;
import com.wlsj.request.Request;
import com.wlsj.response.Response;

import java.io.IOException;

public interface RequestDispatcher {
    void forward(Request request, Response response) throws ServletException, IOException;
}
