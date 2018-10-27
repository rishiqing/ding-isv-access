package com.dingtalk.isv.access.biz.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 用于异步执行的executor工厂方法
 * Created by Wallace on 2018/10/19.
 */
public class AsyncOrderChargeExecutorFactory {
    public Executor getOrderChargeExecutor(){
        //  建立默认10个线程的线程池
        return Executors.newFixedThreadPool(5);
    }
}
