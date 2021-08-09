package com.wlsj.exception;

import com.wlsj.enumeration.HttpStatus;
import com.wlsj.exception.base.ServletException;


public class ResourceNotFoundException extends ServletException {
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException() {
        super(status);
    }

    public ResourceNotFoundException(String meaasge) {
        super(meaasge, status);
    }
}
