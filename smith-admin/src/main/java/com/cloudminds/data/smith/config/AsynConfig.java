package com.cloudminds.data.smith.config;

import cn.hutool.json.JSONUtil;
import com.cloudminds.data.smith.exception.BaseServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步处理配置
 *
 * @author Tao.Liu
 * @date 2022/6/30 16:34
 */
@Slf4j
@Configuration
@EnableAsync
public class AsynConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "defaultAsyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        executor.setThreadNamePrefix("default-async-exec-");
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(300);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            if (BaseServiceException.class.isInstance(throwable)) {
                log.warn("{}#{} 业务异常---> {}", method.getDeclaringClass().getName(), method.getName(), throwable.getMessage());
            } else {
                log.error("asyncExceptionHandler exception message - {},method name - {},parameter value - {}",
                        throwable.getMessage(), method.getName(), JSONUtil.toJsonStr(objects), throwable);
            }
        };
    }
}
