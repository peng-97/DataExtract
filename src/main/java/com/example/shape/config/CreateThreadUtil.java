package com.example.shape.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public  class CreateThreadUtil {

    private static final int THREAD_POOL_SIZE = 4;//线程数量

    public static ThreadPoolExecutor createThread(String guid)//传入一个线程池名字，可随意
    {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(guid).build();//创建线程工厂

        //创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(THREAD_POOL_SIZE,
                THREAD_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}