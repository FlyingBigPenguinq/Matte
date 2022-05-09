package com.syl.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Liu XiangLiang
 * @description: Thread Pool Configcation
 * @date 2022/5/1 下午3:55
 */
@Slf4j
public class AsyncExecutePool implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(4);

        executor.setMaxPoolSize(8);

        executor.setQueueCapacity(8);

        executor.setKeepAliveSeconds(50);

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        executor.setThreadNamePrefix("Lxl-syl-Thread");

        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler = new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                log.error(">> "+throwable.getMessage()+" <<", throwable);
                log.error(">> exception method:"+method.getName() + " <<");
            }
        };
        return asyncUncaughtExceptionHandler;
    }
}
