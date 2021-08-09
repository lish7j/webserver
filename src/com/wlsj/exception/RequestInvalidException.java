package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class RequestInvalidException extends ServletException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public RequestInvalidException() {
        super(status);
    }

    public RequestInvalidException(String message) {
        super(message, status);
    }
}
