package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;

public class TemplateResolveException extends ServletException {
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public TemplateResolveException() {
        super(status);
    }

    public TemplateResolveException(String message) {
        super(message, status);
    }
}
