package com.github.ku4marez.catalog.configuration;

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
    public static final String PRODUCT_BY_ID   = "productById";
    public static final String PRODUCT_BY_SLUG = "productBySlug";

    @Bean
    public CacheManager cacheManager() {
        var cm = new CaffeineCacheManager(PRODUCT_BY_ID, PRODUCT_BY_SLUG);
        cm.setCaffeine(Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10)));
        cm.setAllowNullValues(false);
        return cm;
    }
}
