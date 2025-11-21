package com.github.ku4marez.order.service;

import com.github.ku4marez.order.configuration.OrderEventsPublisher;
import com.github.ku4marez.order.dto.OrderCreateRequest;
import com.github.ku4marez.order.dto.OrderOptionResponse;
import com.github.ku4marez.order.dto.OrderResponse;
import com.github.ku4marez.order.entity.OrderEntity;
import com.github.ku4marez.order.entity.OrderStatus;
import com.github.ku4marez.order.exception.OrderNotFoundException;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.github.ku4marez.order.constant.ApplicationConstant.ORDER_BY_ID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ORDER_BY_ID)
public class OrderService {

    private final OrderRepository repo;
    private final OrderMapper mapper;
    private final OrderEventsPublisher publisher;

    // ---------- Commands ----------
    @Transactional
    @Caching(evict = {
        @CacheEvict(key = "#result.id", condition = "#result != null")
    })
    public OrderResponse create(String idempotencyKey, OrderCreateRequest req) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            Optional<OrderEntity> existing = repo.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) return mapper.toResponse(existing.get());
        }

        var e = mapper.toNewEntity(req);
        e.setIdempotencyKey(idempotencyKey);

        var total = req.items().stream()
            .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        e.setTotalAmount(total);

        e.setStatus(OrderStatus.NEW);

        var saved = repo.save(e);
        publisher.publishOrderCreated(saved);
        return mapper.toResponse(saved);
    }

    @Transactional
    @CacheEvict(key = "#id")
    public OrderResponse updateStatus(String id, OrderStatus status) {
        var e = repo.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        e.setStatus(status);
        var saved = repo.save(e);

        if (status == OrderStatus.CANCELLED) publisher.publishOrderCancelled(saved, "manual or saga");
        else if (status == OrderStatus.CONFIRMED) publisher.publishOrderConfirmed(saved);

        return mapper.toResponse(saved);
    }

    // ---------- Queries ----------
    @Cacheable(key = "#id")
    public OrderResponse get(String id) {
        var e = repo.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        return mapper.toResponse(e);
    }

    public Page<OrderResponse> listByCustomer(String customerId, Pageable pageable) {
        return repo.findByCustomerIdOrderByCreationDateDesc(customerId, pageable)
            .map(mapper::toResponse);
    }

    public Page<OrderResponse> listAll(String customerId, OrderStatus status,
                                       Instant from, Instant to, Pageable pageable) {
        return repo.search(customerId, status, from, to, pageable)
            .map(mapper::toResponse);
    }

    public List<OrderOptionResponse> listOptions(String search, OrderStatus status, int limit) {
        return repo.findOptions(search, status, limit).stream()
            .map(e -> new OrderOptionResponse(e.getId(), e.getCustomerId(), e.getStatus(), e.getTotalAmount()))
            .toList();
    }

}
