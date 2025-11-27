package com.github.ku4marez.ecom.starters.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecom.resilience.circuit-breaker")
public class CircuitBreakerProperties {
    private Integer failureRateThreshold = 50;
    private Integer slowCallRateThreshold = 50;
    private Integer permittedNumberOfCallsInHalfOpenState = 10;
    private Integer slidingWindowSize = 100;
    private Integer slowCallDurationThresholdMs = 1000;
    private Integer waitDurationInOpenStateSeconds = 20;

    public Integer getFailureRateThreshold() {
        return failureRateThreshold;
    }

    public void setFailureRateThreshold(Integer failureRateThreshold) {
        this.failureRateThreshold = failureRateThreshold;
    }

    public Integer getSlowCallRateThreshold() {
        return slowCallRateThreshold;
    }

    public void setSlowCallRateThreshold(Integer slowCallRateThreshold) {
        this.slowCallRateThreshold = slowCallRateThreshold;
    }

    public Integer getPermittedNumberOfCallsInHalfOpenState() {
        return permittedNumberOfCallsInHalfOpenState;
    }

    public void setPermittedNumberOfCallsInHalfOpenState(Integer permittedNumberOfCallsInHalfOpenState) {
        this.permittedNumberOfCallsInHalfOpenState = permittedNumberOfCallsInHalfOpenState;
    }

    public Integer getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public void setSlidingWindowSize(Integer slidingWindowSize) {
        this.slidingWindowSize = slidingWindowSize;
    }

    public Integer getSlowCallDurationThresholdMs() {
        return slowCallDurationThresholdMs;
    }

    public void setSlowCallDurationThresholdMs(Integer slowCallDurationThresholdMs) {
        this.slowCallDurationThresholdMs = slowCallDurationThresholdMs;
    }

    public Integer getWaitDurationInOpenStateSeconds() {
        return waitDurationInOpenStateSeconds;
    }

    public void setWaitDurationInOpenStateSeconds(Integer waitDurationInOpenStateSeconds) {
        this.waitDurationInOpenStateSeconds = waitDurationInOpenStateSeconds;
    }
}
