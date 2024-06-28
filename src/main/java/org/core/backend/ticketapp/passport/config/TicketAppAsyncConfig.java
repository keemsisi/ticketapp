package org.core.backend.ticketapp.passport.config;

import org.core.backend.ticketapp.passport.error.TicketAppAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync(proxyTargetClass = true)
@Configuration
public class TicketAppAsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        var threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        threadPoolTaskExecutor.setThreadNamePrefix("TicketApp-AsyncThreadPool-");
        threadPoolTaskExecutor.setThreadGroupName("TicketApp-AsyncThreadPool");
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new TicketAppAsyncExceptionHandler();
    }
}
