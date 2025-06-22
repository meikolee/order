package com.example.demo.thread;

import java.util.Arrays;

public class Thread2 implements Runnable{
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // 设置线程属性
        Thread.currentThread().setName("线程2");// 设置线程名称
        //Thread.currentThread().setDaemon(false); // 设置线程为守护线程 默认为否守护线程
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);// 设置线程优先级 策略为 1-10
        Thread.currentThread().setContextClassLoader(Thread.currentThread().getContextClassLoader());// 设置线程上下文类加载器
        System.out.println("线程2开始执行");
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getPriority());
        System.out.println(Thread.currentThread().isDaemon());
        System.out.println(Thread.currentThread().getThreadGroup());
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        System.out.println(Thread.currentThread().getState());
        System.out.println(Thread.currentThread().getUncaughtExceptionHandler());
    }

    public static void main(String[] args) {
        // 运行run
        new Thread2().run();
    }
}
