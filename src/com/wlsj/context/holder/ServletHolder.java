package com.wlsj.context.holder;

import com.wlsj.servlet.Servlet;

public class ServletHolder {
    private Servlet servlet;
    private String servletClass;

    public ServletHolder(String servletClass) {
        this.servletClass = servletClass;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }
}
