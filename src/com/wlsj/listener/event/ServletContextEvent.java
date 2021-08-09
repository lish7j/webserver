package com.wlsj.listener.event;

import com.wlsj.context.ServletContext;

public class ServletContextEvent extends java.util.EventObject{
    private static final long serialVersionUID = -1;

    public ServletContextEvent(ServletContext source) {
        super(source);
    }

    public ServletContext getServletContext() {
        return (ServletContext)super.getSource();
    }

}
