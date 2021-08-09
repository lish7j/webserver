package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class ServletNotFoundException extends ServletException {
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ServletNotFoundException() {
        super(httpStatus);
    }

    public ServletNotFoundException(String message) {
        super(message, httpStatus);
    }
}
