package com.dingtalk.isv.access.web.test;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Date;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: user 毛文强
 * Date: 2017/11/23
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class TestDemo {
    public static void main(String[] args) {
        //ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10,
        //        0L, TimeUnit.MILLISECONDS,
        //        new LinkedBlockingQueue<Runnable>());
        Executor executor = Executors.newFixedThreadPool(10);
        AsyncEventBus asyncEventBus = new AsyncEventBus("asyncEventBus", executor);
        /**
         * 注册事件处理器
         */
        asyncEventBus.register(new Object(){
            @Subscribe
            @AllowConcurrentEvents
            public void handleUserInfoChangeEvent(String msg){
                System.out.println(">>--" + msg +" --start--" + new Date());
                try {
                    System.out.println(">>--" + msg + " ----thread id----" + Thread.currentThread().getId());
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(">>--" + msg + " --end--: " + new Date());
            }
        });
        asyncEventBus.post("apple");
        asyncEventBus.post("banana");
        asyncEventBus.post("orange");
        asyncEventBus.post("peach");
        System.out.println("异步EventBus");

        //ExecutorService executor = Executors.newFixedThreadPool(5);
        //for (int i = 0; i < 10; i++) {
        //    Runnable worker = new DemoWorkerThread("-t-" + i);
        //    executor.execute(worker);
        //}
        //executor.shutdown();
        //while (!executor.isTerminated()) {
        //}
        //System.out.println("Finished all threads");
    }
}
