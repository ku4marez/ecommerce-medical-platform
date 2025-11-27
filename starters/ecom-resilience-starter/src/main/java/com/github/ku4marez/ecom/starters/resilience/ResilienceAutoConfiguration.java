package com.github.ku4marez.ecom.starters.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@AutoConfiguration
@EnableConfigurationProperties({ResilienceProperties.class, CircuitBreakerProperties.class, RateLimiterProperties.class,
    TimeLimiterProperties.class, RetryProperties.class, TimeLimiterProperties.class})
@ConditionalOnProperty(prefix = "ecom.resilience", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResilienceAutoConfiguration {

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig(ResilienceProperties props) {
        CircuitBreakerProperties cb = props.getCircuitBreaker();

        return CircuitBreakerConfig.custom()
            .failureRateThreshold(cb.getFailureRateThreshold())
            .slowCallRateThreshold(cb.getSlowCallRateThreshold())
            .permittedNumberOfCallsInHalfOpenState(cb.getPermittedNumberOfCallsInHalfOpenState())
            .slidingWindowSize(cb.getSlidingWindowSize())
            .slowCallDurationThreshold(Duration.ofMillis(cb.getSlowCallDurationThresholdMs()))
            .waitDurationInOpenState(Duration.ofSeconds(cb.getWaitDurationInOpenStateSeconds()))
            .build();
    }

    @Bean
    public RetryConfig retryConfig(RetryProperties props) {

        return RetryConfig.custom()
            .maxAttempts(props.getMaxAttempts())
            .waitDuration(Duration.ofMillis(props.getWaitDurationMs()))
            .build();
    }

    @Bean
    public RateLimiterConfig rateLimiterConfig(RateLimiterProperties props) {
        return RateLimiterConfig.custom()
            .limitForPeriod(props.getLimitForPeriod())
            .limitRefreshPeriod(Duration.ofSeconds(props.getLimitRefreshPeriodSeconds()))
            .timeoutDuration(Duration.ofSeconds(props.getTimeoutDurationSeconds()))
            .build();
    }

    @Bean
    public TimeLimiterConfig timeLimiterConfig(TimeLimiterProperties props) {

        return TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(props.getTimeoutSeconds()))
            .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig config) {
        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public RetryRegistry retryRegistry(RetryConfig config) {
        return RetryRegistry.of(config);
    }

    @Bean
    public RateLimiterRegistry rateLimiterRegistry(RateLimiterConfig config) {
        return RateLimiterRegistry.of(config);
    }

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry(TimeLimiterConfig config) {
        return TimeLimiterRegistry.of(config);
    }
}
