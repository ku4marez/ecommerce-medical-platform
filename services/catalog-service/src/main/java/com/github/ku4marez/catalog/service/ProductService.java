package com.github.ku4marez.catalog.service;

import com.github.ku4marez.catalog.configuration.CacheConfiguration;
import com.github.ku4marez.catalog.configuration.ProductEventsPublisher;
import com.github.ku4marez.catalog.dto.ProductCreateRequest;
import com.github.ku4marez.catalog.dto.ProductUpdateRequest;
import com.github.ku4marez.catalog.entity.ProductEntity;
import com.github.ku4marez.catalog.mapper.ProductMapper;
import com.github.ku4marez.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = { CacheConfiguration.PRODUCT_BY_ID, CacheConfiguration.PRODUCT_BY_SLUG })
public class ProductService {
    private final ProductRepository repo;
    private final ProductEventsPublisher publisher;
    private final ProductMapper mapper;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = CacheConfiguration.PRODUCT_BY_ID, key = "#id")
    public ProductEntity getByIdCached(String id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @Cacheable(cacheNames = CacheConfiguration.PRODUCT_BY_SLUG, key = "#slug")
    public ProductEntity getBySlugCached(String slug) {
        return repo.findBySlug(slug).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public Page<ProductEntity> list(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<ProductEntity> search(String q, Pageable pageable) {
        if (q == null || q.isBlank()) return list(pageable);
        return repo.textSearch(q, pageable);
    }

    @Transactional
    @Caching(put = {
        // warm caches immediately after create
        @CachePut(cacheNames = CacheConfiguration.PRODUCT_BY_ID,   key = "#result.id"),
        @CachePut(cacheNames = CacheConfiguration.PRODUCT_BY_SLUG, key = "#result.slug")
    })
    public ProductEntity create(ProductCreateRequest r) {
        repo.findBySku(r.sku()).ifPresent(x -> { throw new IllegalArgumentException("SKU exists"); });
        repo.findBySlug(r.slug()).ifPresent(x -> { throw new IllegalArgumentException("Slug exists"); });

        var saved = repo.save(mapper.toNewEntity(r));
        publisher.publishProductUpdated(saved.getId(),
            "{\"type\":\"product.created\",\"id\":\""+saved.getId()+"\"}");
        return saved;
    }

    @Transactional
    @Caching(evict = {
        // slug may change; evict by old keys and new keys
        @CacheEvict(cacheNames = CacheConfiguration.PRODUCT_BY_ID,   key = "#id"),
        @CacheEvict(cacheNames = CacheConfiguration.PRODUCT_BY_SLUG, key = "#result.slug", condition = "#result != null")
    },
        put = {
            // repopulate caches with the updated entity
            @CachePut(cacheNames = CacheConfiguration.PRODUCT_BY_ID,   key = "#result.id"),
            @CachePut(cacheNames = CacheConfiguration.PRODUCT_BY_SLUG, key = "#result.slug")
        })
    public ProductEntity update(String id, ProductUpdateRequest r) {
        var e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        var oldSlug = e.getSlug();
        mapper.updateEntity(e, r);
        var saved = repo.save(e);

        // if slug changed, evict old slug entry explicitly
        if (oldSlug != null && !oldSlug.equals(saved.getSlug())) {
            // best-effort extra eviction; safe even if missing
            Optional.ofNullable(cacheManager.getCache(CacheConfiguration.PRODUCT_BY_SLUG))
                .ifPresent(c -> c.evict(oldSlug));
        }

        publisher.publishProductUpdated(saved.getId(),
            "{\"type\":\"product.updated\",\"id\":\""+saved.getId()+"\"}");
        return saved;
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = CacheConfiguration.PRODUCT_BY_ID,   key = "#id"),
        // we need slug to evict the slug cache; load once
        @CacheEvict(cacheNames = CacheConfiguration.PRODUCT_BY_SLUG, key = "#result", condition = "#result != null")
    })
    public String delete(String id) {
        var e = repo.findById(id).orElseThrow();
        repo.delete(e);
        return e.getSlug(); // returned solely for eviction via @CacheEvict(condition)
    }
}

