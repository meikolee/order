package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean("orderExecutor")
    public Executor orderExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 线程池核心线程数
        executor.setMaxPoolSize(20); // 线程池最大线程数
        executor.setQueueCapacity(100); // 线程池任务队列容量
        executor.setThreadNamePrefix("order_exce-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略为调用者运行
        executor.initialize(); // 初始化线程池
        return executor; // 返回线程池
    }
}
