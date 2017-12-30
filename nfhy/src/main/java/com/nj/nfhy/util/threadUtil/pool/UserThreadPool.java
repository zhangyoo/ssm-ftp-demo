package com.nj.nfhy.util.threadUtil.pool;

import com.nj.nfhy.util.threadUtil.factory.UserThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class UserThreadPool {
    public static final int poolSize = 10;

    public static ThreadPoolExecutor userThreadPool;

    static {
        if (userThreadPool == null){
            userThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize, new UserThreadFactory());
        }
    }
}
