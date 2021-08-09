package com.wlsj.network.handler;


import com.wlsj.context.ServletContext;
import com.wlsj.exception.FilterNotFoundException;
import com.wlsj.exception.ServerErrorException;
import com.wlsj.exception.ServletNotFoundException;
import com.wlsj.exception.base.ServletException;
import com.wlsj.exception.handler.ExceptionHandler;
import com.wlsj.filter.Filter;
import com.wlsj.filter.FilterChain;
import com.wlsj.network.wrapper.SocketWrapper;
import com.wlsj.request.Request;
import com.wlsj.resource.ResourceHandler;
import com.wlsj.response.Response;
import com.wlsj.servlet.Servlet;

import java.io.FileNotFoundException;
import java.util.List;

public abstract class AbstractRequestHandler implements FilterChain, Runnable {
    protected Request request;
    protected Response response;
    protected SocketWrapper socketWrapper;
    protected ServletContext servletContext;
    protected ExceptionHandler exceptionHandler;
    protected ResourceHandler resourceHandler;
    protected boolean isFinished;
    protected Servlet servlet;
    protected List<Filter> filters;
    private int filterIndex = 0;

    public AbstractRequestHandler(SocketWrapper socketWrapper, ServletContext servletContext,
                                  ExceptionHandler exceptionHandler, ResourceHandler resourceHandler,
                                  Request request, Response response) throws ServletNotFoundException, FilterNotFoundException
    {
        this.socketWrapper = socketWrapper;
        this.servletContext = servletContext;
        this.exceptionHandler = exceptionHandler;
        this.resourceHandler = resourceHandler;
        this.request = request;
        this.response = response;
        this.isFinished = false;
        this.request.setServletContext(servletContext);
        this.request.setRequestHandler(this);
        this.response.setRequestHandler(this);
        servlet = servletContext.mapServlet(request.getUrl());
        filters = servletContext.mapFilter(request.getUrl());
    }

    @Override
    public void doFilter(Request request, Response response) {
        if (filterIndex < filters.size()) {
            filters.get(filterIndex++).doFilter(request, response, this);
        } else {
            service();
        }
    }

    @Override
    public void run() {

        if (filters.isEmpty()) {
            service();
        } else {
            doFilter(request, response);
        }
    }

    public void service() {
        try {
            servlet.service(request, response);
            //flushResponse();
        } catch (ServletException  e) {
            exceptionHandler.handle(e, response, socketWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionHandler.handle(new ServerErrorException(), response, socketWrapper);
        } finally {
            if (!isFinished) {
                flushResponse();
            }
        }
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public SocketWrapper getSocketWrapper() {
        return socketWrapper;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public int getFilterIndex() {
        return filterIndex;
    }

    public abstract void flushResponse();
}
