package com.wlsj.network.dispatcher;

import com.wlsj.context.ServletContext;
import com.wlsj.context.WebApplication;
import com.wlsj.exception.handler.ExceptionHandler;
import com.wlsj.network.wrapper.SocketWrapper;
import com.wlsj.resource.ResourceHandler;

import java.util.concurrent.*;

public abstract class AbstractDispatcher {
    protected ResourceHandler resourceHandler;
    protected ExceptionHandler exceptionHandler;
    protected ThreadPoolExecutor executor;
    protected ServletContext servletContext;

    public AbstractDispatcher() {
        this.servletContext = WebApplication.getServletContext();
        this.exceptionHandler = new ExceptionHandler();
        this.resourceHandler = new ResourceHandler(exceptionHandler);
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Worker Pool-" + count++);
            }
        };
        this.executor = new ThreadPoolExecutor(100, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void shutdown() {
        executor.shutdown();
        servletContext.destroy();
    }

    public abstract void doDispatch(SocketWrapper socketWrapper);


}
