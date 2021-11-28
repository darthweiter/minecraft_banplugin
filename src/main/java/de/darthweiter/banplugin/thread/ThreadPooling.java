package de.darthweiter.banplugin.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPooling {

    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    public static void addRunnable(Runnable task) {
        executor.execute(task);
    }

    public static void init() {
        executor.setKeepAliveTime(60, TimeUnit.SECONDS);
        executor.setMaximumPoolSize(5);
        executor.setCorePoolSize(2);
    }


}
