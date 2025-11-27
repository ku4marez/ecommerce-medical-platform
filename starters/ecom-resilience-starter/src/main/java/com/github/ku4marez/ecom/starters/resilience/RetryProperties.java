package com.github.ku4marez.ecom.starters.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecom.resilience.retry")
public class RetryProperties {
    private Integer maxAttempts = 3;
    private Integer waitDurationMs = 500;

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getWaitDurationMs() {
        return waitDurationMs;
    }

    public void setWaitDurationMs(Integer waitDurationMs) {
        this.waitDurationMs = waitDurationMs;
    }
}
