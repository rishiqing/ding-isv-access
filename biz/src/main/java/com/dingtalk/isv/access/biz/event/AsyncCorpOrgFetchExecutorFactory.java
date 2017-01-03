package com.dingtalk.isv.access.biz.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 用于异步执行的executor工厂方法
 * Created by Wallace on 2016/12/19.
 */
public class AsyncCorpOrgFetchExecutorFactory {
    public Executor getCorpOrgFetchExecutor(){
        //  建立默认10个线程的线程池
        return Executors.newFixedThreadPool(10);
    }
}
