package com.github.ku4marez.inventory.service;

import java.time.Duration;

public interface CacheService<T> {
    T get(String key, Class<T> type);
    void set(String key, T value, Duration ttl);
    void evict(String key);
}
