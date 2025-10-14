package com.github.ku4marez.inventory.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String STOCK_BY_PRODUCT = "stockByProduct";

    @Bean
    public CacheManager cacheManager() {
        var cm = new CaffeineCacheManager(
            STOCK_BY_PRODUCT
        );
        cm.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(10))
        );
        cm.setAllowNullValues(false);
        return cm;
    }
}
