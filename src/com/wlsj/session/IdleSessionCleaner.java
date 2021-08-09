package com.wlsj.session;

import com.wlsj.context.WebApplication;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IdleSessionCleaner implements Runnable{
    private ScheduledExecutorService executorService;

    public IdleSessionCleaner() {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "IdleSessionCleaner");
            }
        };
        this.executorService = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }



    public void start() {
        executorService.scheduleAtFixedRate(this, 5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        WebApplication.getServletContext().cleanIdleSessions();
    }
}
