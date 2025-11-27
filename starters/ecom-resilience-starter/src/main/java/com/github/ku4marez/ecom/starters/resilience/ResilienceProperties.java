package com.github.ku4marez.ecom.starters.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecom.resilience")
public class ResilienceProperties {
    private CircuitBreakerProperties circuitBreaker;

    public CircuitBreakerProperties getCircuitBreaker() {
        return circuitBreaker;
    }

    public void setCircuitBreaker(CircuitBreakerProperties circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }
}
