package org.core.backend.ticketapp.passport.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
public class TicketAppAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
        log.error("----||||ERROR OCCURRED WHILE PROCESSING ASYNC METHOD " + method.getName());
        log.error("{0}", throwable);
    }
}
