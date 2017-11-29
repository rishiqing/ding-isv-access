package com.dingtalk.isv.access.web.test;

/**
 * Created with IntelliJ IDEA.
 * User: user 毛文强
 * Date: 2017/11/23
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class DemoWorkerThread implements Runnable {
    private String command;

    public DemoWorkerThread(String s){
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}
