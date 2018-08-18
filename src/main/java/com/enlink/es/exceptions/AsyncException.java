package com.enlink.es.exceptions;

import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncException implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        LOGGER.info("Async method: {} has uncaught exception, params: {}",
                method.getName(),
                new GsonBuilder().create().toJson(params));
        ex.printStackTrace();
    }
}
