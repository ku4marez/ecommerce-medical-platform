package com.github.ku4marez.inventory.configuration;

import com.github.ku4marez.inventory.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

    private final CacheService<Object> cacheService;

    @Around("@annotation(cached)")
    public Object handleCache(ProceedingJoinPoint pjp, Cache cached) throws Throwable {
        Class<?> returnType = ((MethodSignature) pjp.getSignature()).getReturnType();

        String key = cached.prefix() + "-" + cached.key();

        Object cachedValue = cacheService.get(key, (Class<Object>) returnType);
        if (cachedValue != null) {
            return cachedValue;
        }

        Object result = pjp.proceed();
        cacheService.set(key, result, Duration.ofSeconds(cached.ttl()));

        return result;
    }

    @After("@annotation(cacheEvict)")
    public void handleCacheEvict(CacheEvict cacheEvict) {
        String key = cacheEvict.prefix() + "-" + cacheEvict.key();
        cacheService.evict(key);
    }
}
