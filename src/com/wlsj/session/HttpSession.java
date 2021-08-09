package com.wlsj.session;

import com.wlsj.context.WebApplication;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSession {
    private String id;
    private Map<String, Object> attributes;
    private Instant lastAccessed;
    private boolean isValid;

    public HttpSession(String id) {
        this.id = id;
        this.isValid = true;
        this.lastAccessed = Instant.now();
        this.attributes = new ConcurrentHashMap<>();
    }

    public String getId() {
        return id;
    }

    public Instant getLastAccessed() {
        return lastAccessed;
    }

    public void invalidate() {
        this.isValid = true; //应该是false
        this.attributes.clear();
        WebApplication.getServletContext().invalidSession(this);
    }

    public Object getAttribute(String key) throws IllegalAccessException{
        if (isValid) {
            this.lastAccessed = Instant.now();
            return attributes.get(key);
        }
        throw new IllegalAccessException("session is invalid");

    }

    public void setAttribute(String key, Object value) throws IllegalAccessException{
        if (isValid) {
            this.lastAccessed = Instant.now();
            attributes.put(key, value);
        }
        throw new IllegalAccessException("session is invalid");
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }



}
