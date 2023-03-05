package com.shundei.netty.common;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * 
 * @author huoyouri
 */
public class ThreadPoolUtils {
    
    private static final ThreadPoolExecutor CACHE_REBUILD_EXECUTOR  =
            new ThreadPoolExecutor(
                    // 核心线程数
                    10,
                    // 最大线程数
                    20,
                    // 超过核心线程数量的线程(救急线程)最大空闲时间
                    2,
                    // 时间单位
                    TimeUnit.SECONDS,
                    // 任务队列
                    new ArrayBlockingQueue<>(2),
                    // 拒绝策略
                    new ThreadPoolExecutor.AbortPolicy()
            );

    /**
     * 无响应执行
     */
    public static void execute(Runnable runnable){
        CACHE_REBUILD_EXECUTOR.execute(runnable);
    }

    /**
     * 有响应执行
     */
    public static<T> Future<T> submit(Callable<T> callable){
        return CACHE_REBUILD_EXECUTOR.submit(callable);
    }
}
