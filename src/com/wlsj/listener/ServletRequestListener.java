package com.wlsj.listener;

import com.wlsj.listener.event.ServletRequestEvent;

import java.util.EventListener;

public interface ServletRequestListener extends EventListener {
    void requestInitialized(ServletRequestEvent requestEvent);

    void requestDestoyed(ServletRequestEvent requestEvent);
}
