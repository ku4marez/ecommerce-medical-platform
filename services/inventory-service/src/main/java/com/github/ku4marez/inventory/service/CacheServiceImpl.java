package com.github.ku4marez.inventory.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl<T> implements CacheService<T> {
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    @Override
    public T get(String key, Class<T> type) {
        CacheEntry cacheEntry = cache.get(key);
        if (cacheEntry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return type.cast(cacheEntry);
    }

    @Override
    public void set(String key, T value, Duration ttl) {
        if (cache.containsKey(key)) {
            evict(key);
        }
        cache.put(key, new CacheEntry(value, Instant.now().plus(ttl)));
    }

    @Override
    public void evict(String key) {
        cache.remove(key);
    }

    public record CacheEntry(Object entry, Instant expiresAt) {
        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
