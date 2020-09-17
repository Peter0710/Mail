package com.leo.cart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Liu
 */
@Configuration
public class BlogThreadPool {

    @Value("${blog.mainthreadpool.corePoolSize}")
    int corePoolSize;

    @Value("${blog.mainthreadpool.maximumPoolSize}")
    int maximumPoolSize;

    @Value("${blog.mainthreadpool.keepAliveTime}")
    long keepAliveTime;

    @Value("${blog.mainthreadpool.queueSize}")
    int queueSize;

    /**
     * 核心业务线程池
     * int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue
     * @return
     */
    @Bean("mainThreadPool")
    public ThreadPoolExecutor mainThreadPoolExecutor(){
        return  new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(queueSize));
    }

    /**
     * 非核心业务线程池
     * @return
     */
//    @Bean("threadPool")
//    public ThreadPoolExecutor coreThreadPoolExecutor(){
//        return  new ThreadPoolExecutor();
//    }
}
