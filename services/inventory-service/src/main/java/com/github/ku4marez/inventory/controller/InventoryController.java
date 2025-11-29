package com.github.ku4marez.inventory.controller;

import com.github.ku4marez.inventory.dto.api.*;
import com.github.ku4marez.inventory.entity.ReservationStatus;
import com.github.ku4marez.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    // === Queries ===

    @GetMapping("/stock/{productId}")
    public StockItemResponse getStock(@PathVariable String productId) {
        return service.getStock(productId);
    }

    @GetMapping("/reservations/{productId}/{orderId}")
    public ReservationResponse getReservation(@PathVariable String productId, @PathVariable String orderId) {
        return service.getReservation(productId, orderId);
    }

    /** Paginated list of stock items (for admin views, dropdowns, etc.). */
    @GetMapping("/stock")
    public Page<StockItemResponse> listStock(
        @RequestParam(required = false) String search,
        Pageable pageable
    ) {
        return service.listStock(search, pageable);
    }

    /** Paginated reservations with filters by status, productId, or orderId. */
    @GetMapping("/reservations")
    public Page<ReservationResponse> listReservations(
        @RequestParam(required = false) String productId,
        @RequestParam(required = false) String orderId,
        @RequestParam(required = false) ReservationStatus status,
        Pageable pageable
    ) {
        return service.listReservations(productId, orderId, status, pageable);
    }

    // === Commands ===

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse reserve(@Valid @RequestBody ReserveRequest req) {
        return service.reserve(req);
    }

    @PostMapping("/release")
    public ReservationResponse release(@Valid @RequestBody ReleaseRequest req) {
        return service.release(req);
    }

    @PostMapping("/confirm")
    public ReservationResponse confirm(@Valid @RequestBody ConfirmRequest req) {
        return service.confirm(req);
    }

    @PostMapping("/adjust")
    public StockItemResponse adjust(@Valid @RequestBody AdjustStockRequest req) {
        return service.adjust(req);
    }
}

