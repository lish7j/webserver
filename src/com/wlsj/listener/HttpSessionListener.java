package com.wlsj.listener;


import com.wlsj.listener.event.HttpSessionEvent;

import java.util.EventListener;

public interface HttpSessionListener extends EventListener {

    void sessionCreated(HttpSessionEvent sessionEvent);

    void sessionDestroyed(HttpSessionEvent sessionEvent);
}
