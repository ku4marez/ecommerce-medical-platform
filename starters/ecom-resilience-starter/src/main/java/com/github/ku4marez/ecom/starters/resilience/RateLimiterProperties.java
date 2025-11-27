package com.github.ku4marez.ecom.starters.resilience;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecom.resilience.rate-limiter")
public class RateLimiterProperties {
    private Integer limitForPeriod = 10;
    private Integer limitRefreshPeriodSeconds = 1;
    private Integer timeoutDurationSeconds = 2;

    public Integer getLimitForPeriod() {
        return limitForPeriod;
    }

    public void setLimitForPeriod(Integer limitForPeriod) {
        this.limitForPeriod = limitForPeriod;
    }

    public Integer getLimitRefreshPeriodSeconds() {
        return limitRefreshPeriodSeconds;
    }

    public void setLimitRefreshPeriodSeconds(Integer limitRefreshPeriodSeconds) {
        this.limitRefreshPeriodSeconds = limitRefreshPeriodSeconds;
    }

    public Integer getTimeoutDurationSeconds() {
        return timeoutDurationSeconds;
    }

    public void setTimeoutDurationSeconds(Integer timeoutDurationSeconds) {
        this.timeoutDurationSeconds = timeoutDurationSeconds;
    }
}
