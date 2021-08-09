package com.wlsj.context;

public class WebApplication {
    private static ServletContext servletContext;

    static {
        try {
            servletContext = new ServletContext();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

}
