package com.wlsj.listener.event;

import com.wlsj.session.HttpSession;

public class HttpSessionEvent extends java.util.EventObject {
    private static final long serialVersionUID = -7622791603672342895L;

    public HttpSessionEvent(HttpSession source) {
        super(source);
    }

    public HttpSession getSession() {
        return (HttpSession)source;
    }
}
