package com.github.ku4marez.ecom.starters.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecom.resilience.time-limiter")
public class TimeLimiterProperties {
    private Integer timeoutSeconds = 5;

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
