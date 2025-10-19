package com.github.ku4marez.inventory.service;

import com.github.ku4marez.inventory.configuration.StockEventsPublisher;
import com.github.ku4marez.inventory.dto.*;
import com.github.ku4marez.inventory.entity.ReservationEntity;
import com.github.ku4marez.inventory.entity.ReservationStatus;
import com.github.ku4marez.inventory.entity.StockItemEntity;
import com.github.ku4marez.inventory.mapper.ReservationMapper;
import com.github.ku4marez.inventory.mapper.StockItemMapper;
import com.github.ku4marez.inventory.repository.ReservationRepository;
import com.github.ku4marez.inventory.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import com.github.ku4marez.inventory.configuration.CacheConfiguration;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheConfiguration.STOCK_BY_PRODUCT)
public class InventoryService {
    private final ReservationRepository reservations;
    private final StockItemRepository stock;
    private final ReservationMapper reservationMapper;
    private final StockItemMapper stockItemMapper;
    private final MongoTemplate mongo;
    private final StockEventsPublisher publisher;

    // --- Queries ---
    @Cacheable(key = "#productId")
    public StockItemResponse getStock(String productId) {
        var si = stock.findByProductId(productId)
            .orElseGet(() -> {
                var s = new StockItemEntity();
                s.setProductId(productId);
                s.setAvailable(0);
                s.setReserved(0);
                return stock.save(s);
            });
        return stockItemMapper.toResponse(si);
    }

    public ReservationResponse getReservation(String productId, String orderId) {
        var r = reservations.findByProductIdAndOrderId(productId, orderId)
            .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
        return reservationMapper.toResponse(r);
    }

    // --- Commands ---

    /** Idempotent reservation. Decrease available, increase reserved, create Reservation. */
    @Transactional
    @CacheEvict(key = "#req.productId")
    public ReservationResponse reserve(ReserveRequest req) {
        // 1. Idempotency: existing reservation
        var existing = reservations.findByProductIdAndOrderId(req.productId(), req.orderId());
        if (existing.isPresent()) return reservationMapper.toResponse(existing.get());

        // 2. Atomic update of stock counts
        Query q = new Query(Criteria.where("productId").is(req.productId())
            .and("available").gte(req.quantity()));
        Update u = new Update()
            .inc("available", -req.quantity())
            .inc("reserved", req.quantity())
            .currentDate("updatedDate");

        var updated = mongo.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true), StockItemEntity.class);
        if (updated == null) throw new IllegalStateException("Insufficient stock for product " + req.productId());

        // 3. Create reservation
        var entity = new ReservationEntity();
        entity.setProductId(req.productId());
        entity.setOrderId(req.orderId());
        entity.setQuantity(req.quantity());
        entity.setStatus(ReservationStatus.PENDING);
        entity.setExpiresAt(Instant.now().plus(Duration.ofMinutes(10))); // default TTL
        var saved = reservations.save(entity);

        // 4. Publish event
        publisher.stockReserved(saved);

        return reservationMapper.toResponse(saved);
    }

    /** Release reservation back to stock. */
    @Transactional
    @CacheEvict(key = "#req.productId")
    public ReservationResponse release(ReleaseRequest req) {
        var r = reservations.findByProductIdAndOrderId(req.productId(), req.orderId())
            .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
        if (r.getStatus() == ReservationStatus.RELEASED) return reservationMapper.toResponse(r);

        // revert counts
        Query q = new Query(Criteria.where("productId").is(req.productId()));
        Update u = new Update()
            .inc("available", r.getQuantity())
            .inc("reserved", r.getQuantity())
            .currentDate("updatedDate");
        mongo.updateFirst(q, u, StockItemEntity.class);

        r.setStatus(ReservationStatus.RELEASED);
        reservations.save(r);

        publisher.stockReleased(r);
        return reservationMapper.toResponse(r);
    }

    /** Confirm reservation (counts stay same). */
    @Transactional
    public ReservationResponse confirm(ConfirmRequest req) {
        var r = reservations.findByProductIdAndOrderId(req.productId(), req.orderId())
            .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
        if (r.getStatus() == ReservationStatus.CONFIRMED) return reservationMapper.toResponse(r);

        r.setStatus(ReservationStatus.CONFIRMED);
        reservations.save(r);

        publisher.stockConfirmed(r);
        return reservationMapper.toResponse(r);
    }

    /** Manual stock adjustment. */
    @Transactional
    public StockItemResponse adjust(AdjustStockRequest req) {
        Query q = new Query(Criteria.where("productId").is(req.productId()));
        Update u = new Update()
            .inc("available", req.delta())  // add or subtract
            .currentDate("updatedDate");

        var updated = mongo.findAndModify(
            q, u,
            FindAndModifyOptions.options().upsert(true).returnNew(true),
            StockItemEntity.class
        );

        publisher.stockAdjusted(updated, req.reason());
        return stockItemMapper.toResponse(updated);
    }

    public Page<StockItemResponse> listStock(String search, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return stock.findByProductIdContainingIgnoreCase(search, pageable)
                .map(stockItemMapper::toResponse);
        }
        return stock.findAll(pageable).map(stockItemMapper::toResponse);
    }

    public Page<ReservationResponse> listReservations(
        String productId, String orderId, ReservationStatus status, Pageable pageable) {
        return reservations.search(productId, orderId, status, pageable)
            .map(reservationMapper::toResponse);
    }
}

