package com.wlsj.filter;

import com.wlsj.request.Request;
import com.wlsj.response.Response;

public interface FilterChain {
    void doFilter(Request request, Response response);
}
