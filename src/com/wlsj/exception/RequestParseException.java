package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class RequestParseException extends ServletException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public RequestParseException() {
        super(status);
    }

    public RequestParseException(String message) {
        super(message, status);
    }
}
