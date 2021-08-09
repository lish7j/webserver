package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class ServerErrorException extends ServletException {
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServerErrorException() {
        super(status);
    }

    public ServerErrorException(String message) {
        super(message, status);
    }
}
