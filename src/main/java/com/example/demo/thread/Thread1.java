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
            testThreadLocal();
            System.out.println("inheritableThreadLocal : " + inheritableThreadLocal.get());
        });
        t.start();
    }

    // 测试threadlocal
    public static void testThreadLocal() {


        for (int i = 0; i < 10; i++) {
            ThreadLocal<String> threadLocal = new ThreadLocal<>();
            threadLocal.set("threadLocal:" + i);
            System.out.println(threadLocal.get());
        }
        //System.out.println(threadLocal.get());

        //threadLocal.set("threadLocal");
        //System.out.println(threadLocal.get());
    }
}
