package com.github.ku4marez.order.service;

import com.github.ku4marez.order.configuration.CacheConfiguration;
import com.github.ku4marez.order.dto.OrderCreateRequest;
import com.github.ku4marez.order.dto.OrderResponse;
import com.github.ku4marez.order.entity.OrderEntity;
import com.github.ku4marez.order.entity.OrderStatus;
import com.github.ku4marez.order.mapper.OrderMapper;
import com.github.ku4marez.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheConfiguration.ORDER_BY_ID)
public class OrderService {

    private final OrderRepository repo;
    private final OrderMapper mapper;

    // ---------- Queries ----------
    @Cacheable(key = "#id")
    public OrderResponse get(String id) {
        var e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found"));
        return mapper.toResponse(e);
    }

    public Page<OrderResponse> listByCustomer(String customerId, Pageable pageable) {
        return repo.findByCustomerIdOrderByCreationDateDesc(customerId, pageable).map(mapper::toResponse);
    }

    // ---------- Commands ----------
    @Transactional
    @Caching(evict = {
        @CacheEvict(key = "#result.id", condition = "#result != null")
    })
    public OrderResponse create(String idempotencyKey, OrderCreateRequest req) {
        // 1) idempotency
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            Optional<OrderEntity> existing = repo.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) return mapper.toResponse(existing.get());
        }

        // 2) build + compute totals
        var e = mapper.toNewEntity(req);
        e.setIdempotencyKey(idempotencyKey);

        var total = req.items().stream()
            .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        e.setTotalAmount(total);

        // status NEW for now (we'll advance when Kafka/payment is wired)
        e.setStatus(OrderStatus.NEW);

        // 3) save
        var saved = repo.save(e);

        // 4) (later) emit order.created via Kafka publisher

        return mapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(key = "#id")
    public OrderResponse updateStatus(String id, OrderStatus status) {
        var e = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found"));
        e.setStatus(status);
        return mapper.toResponse(repo.save(e));
    }
}
