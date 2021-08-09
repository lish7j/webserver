package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class FilterNotFoundException extends ServletException {
    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public FilterNotFoundException() {
        super(httpStatus);
    }

    public FilterNotFoundException(String message) {
        super(message, httpStatus);
    }
}
