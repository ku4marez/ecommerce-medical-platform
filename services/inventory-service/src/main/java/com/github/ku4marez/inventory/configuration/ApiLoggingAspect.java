package com.github.ku4marez.inventory.configuration;

import com.github.ku4marez.inventory.dto.misc.CustomLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.github.ku4marez.inventory.util.HttpRequestUtils.getMethod;
import static com.github.ku4marez.inventory.util.HttpRequestUtils.getPath;

@Aspect
@Component
public class ApiLoggingAspect {

    @Around("execution(* com.github.ku4marez.inventory.controller..*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;

            CustomLog.info("request",
                "path", getPath(),
                "method", getMethod(),
                "durationMs", duration,
                "status", 200
            );

            return result;
        } catch (Throwable ex) {
            CustomLog.error("error",
                "exception", ex.getClass().getSimpleName(),
                "message", ex.getMessage()
            );
            throw ex;
        }
    }
}

