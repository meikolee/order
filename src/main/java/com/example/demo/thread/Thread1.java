package com.example.demo.thread;

public class Thread1 {
    public static void main(String[] args) throws Exception {
        ThreadLocal threadLocal = new ThreadLocal<>();
        threadLocal.set("本地线程");
        ThreadLocal inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set("分享 - 可继承线程");

        Thread t = new Thread(() -> {
            System.out.println("threadLocal : " + threadLocal.get());
            System.out.println("inheritableThreadLocal : " + inheritableThreadLocal.get());
        });
        t.start();
    }
}
