package com.wlsj.listener;

import com.wlsj.listener.event.ServletContextEvent;

import java.util.EventListener;

public interface ServletContextListener extends EventListener {

    void contextInitialized(ServletContextEvent servletContextEvent);

    void contextDestroyed(ServletContextEvent servletContextEvent);

}
