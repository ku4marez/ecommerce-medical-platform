package com.github.ku4marez.ecom.starters.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ecom.redis")
public class RedisProperties {
    private String host = "localhost";
    private int port = 6379;
    private int ttlSeconds = 3600;
    private String keyPrefix = "default";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTtlSeconds() {
        return ttlSeconds;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }
}
