package com.github.ku4marez.inventory.controller;

import com.github.ku4marez.inventory.dto.*;
import com.github.ku4marez.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

    // Queries
    @GetMapping("/stock/{productId}")
    public StockItemResponse getStock(@PathVariable String productId) {
        return service.getStock(productId);
    }

    @GetMapping("/reservations/{productId}/{orderId}")
    public ReservationResponse getReservation(@PathVariable String productId, @PathVariable String orderId) {
        return service.getReservation(productId, orderId);
    }

    // Commands
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
