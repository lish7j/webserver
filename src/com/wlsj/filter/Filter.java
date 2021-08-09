package com.wlsj.filter;

import com.wlsj.request.Request;
import com.wlsj.response.Response;

public interface Filter {
    void init();

    void doFilter(Request request, Response response, FilterChain filterChain);

    void destroy();
}
