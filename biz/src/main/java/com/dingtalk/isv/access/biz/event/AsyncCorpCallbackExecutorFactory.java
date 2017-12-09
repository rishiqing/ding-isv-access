package com.dingtalk.isv.access.biz.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 用于接受通讯录变更事件执行的executor工厂方法
 * Created by Wallace on 2017/1/4.
 */
public class AsyncCorpCallbackExecutorFactory {
    public Executor getCorpCallbackExecutor(){
        //  建立默认10个线程的线程池
        return Executors.newFixedThreadPool(15);
    }
}
