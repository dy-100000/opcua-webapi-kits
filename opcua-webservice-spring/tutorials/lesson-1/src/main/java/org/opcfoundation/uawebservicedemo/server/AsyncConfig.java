package org.opcfoundation.uawebservicedemo.server;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncConfig {
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 1. 核心线程数：线程池长期维持的线程数量
        executor.setCorePoolSize(5);

        // 2. 最大线程数：线程池允许的最大线程数量
        executor.setMaxPoolSize(10);

        // 3. 队列容量：任务等待队列的长度（超过后触发拒绝策略）
        executor.setQueueCapacity(20); // 队列满后，新任务会被拒绝

        // 4. 线程名前缀：便于日志排查
        executor.setThreadNamePrefix("Async-");

        // 5. 拒绝策略：队列满且线程数达到最大值时，如何处理新任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        // 6. 初始化线程池
        executor.initialize();

        return executor;
    }
}
