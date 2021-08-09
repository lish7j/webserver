package com.wlsj.listener.event;

import com.wlsj.context.ServletContext;
import com.wlsj.request.Request;

public class ServletRequestEvent extends java.util.EventObject {
    private static final long serialVersionUID = -7467864054698729101L;
    private final transient Request request;

    public ServletRequestEvent(ServletContext context, Request request) {
        super(context);
        this.request = request;
    }

    public Request getRequest() {
        return this.request;
    }

    public ServletContext getServletContext() {
        return (ServletContext)source;
    }
}
