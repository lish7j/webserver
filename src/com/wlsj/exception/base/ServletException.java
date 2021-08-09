package com.wlsj.exception.base;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.servlet.Servlet;

import java.io.IOException;

public class ServletException extends Exception {
    private HttpStatus httpStatus;


    public ServletException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ServletException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
