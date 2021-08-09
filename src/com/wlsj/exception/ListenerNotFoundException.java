package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class ListenerNotFoundException extends ServletException {
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public ListenerNotFoundException() {
        super(status);
    }

    public ListenerNotFoundException(String message) {
        super(message, status);
    }
}
