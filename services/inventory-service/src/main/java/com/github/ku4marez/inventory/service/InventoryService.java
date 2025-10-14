package com.github.ku4marez.inventory.service;

import com.github.ku4marez.inventory.configuration.StockEventsPublisher;
import com.github.ku4marez.inventory.dto.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CacheConfiguration.STOCK_BY_PRODUCT)
public class InventoryService {
    private final ReservationRepository reservations;
    private final StockItemRepository stock;
    private final ReservationMapper reservationMapper;
    private final StockItemMapper stockItemMapper;
    private final StockEventsPublisher publisher;

    // --- Queries ---
    @Cacheable(key = "#productId")
    public StockItemResponse getStock(String productId) {
        var si = stock.findByProductId(productId)
            .orElseGet(() -> { var s = new StockItemEntity(); s.setProductId(productId); s.setAvailable(0); s.setReserved(0); return stock.save(s); });
        return stockItemMapper.toResponse(si);
    }

    public ReservationResponse getReservation(String productId, String orderId) {
        var r = reservations.findByProductIdAndOrderId(productId, orderId)
            .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
        return reservationMapper.toResponse(r);
    }

    // --- Commands (implement atomic ops with MongoTemplate findAndModify or transactions) ---

    /** Create or return existing reservation (idempotent). Decrease available, increase reserved. */
    @Transactional
    @CacheEvict(key = "#req.productId")
    public ReservationResponse reserve(ReserveRequest req) {
        // 1) if exists, return it (idempotent)
        // 2) check available >= quantity
        // 3) adjust stock counts atomically
        // 4) create reservation with PENDING + expiresAt(now + ttl)
        // 5) publish stock.reserved
        // TODO implement
        throw new UnsupportedOperationException("reserve not implemented yet");
    }

    /** Mark reservation RELEASED, revert counts, publish stock.released. */
    @Transactional
    @CacheEvict(key = "#req.productId")
    public ReservationResponse release(ReleaseRequest req) {
        // TODO implement
        throw new UnsupportedOperationException("release not implemented yet");
    }

    /** Mark reservation CONFIRMED (counts usually remain; later fulfillment will consume). */
    @Transactional
    public ReservationResponse confirm(ConfirmRequest req) {
        // TODO implement
        throw new UnsupportedOperationException("confirm not implemented yet");
    }

    /** Administrative/manual adjustment of on-hand stock. */
    @Transactional
    public StockItemResponse adjust(AdjustStockRequest req) {
        // TODO implement
        throw new UnsupportedOperationException("adjust not implemented yet");
    }
}

