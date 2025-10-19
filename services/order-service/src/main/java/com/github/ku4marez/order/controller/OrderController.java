package com.github.ku4marez.order.controller;

import com.github.ku4marez.order.dto.OrderCreateRequest;
import com.github.ku4marez.order.dto.OrderOptionResponse;
import com.github.ku4marez.order.dto.OrderResponse;
import com.github.ku4marez.order.entity.OrderStatus;
import com.github.ku4marez.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    // === Queries ===

    /** Get single order by id */
    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable String id) {
        return service.get(id);
    }

    /** List customer orders (for storefront / profile page). */
    @GetMapping
    public Page<OrderResponse> listByCustomer(
        @RequestParam String customerId,
        @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
        Pageable pageable) {
        return service.listByCustomer(customerId, pageable);
    }

    /** List all orders (for admin or partner backend). */
    @GetMapping("/all")
    public Page<OrderResponse> listAll(
        @RequestParam(required = false) String customerId,
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
        Pageable pageable) {
        return service.listAll(customerId, status, from, to, pageable);
    }

    /** Lightweight dropdown list (for admin selects or quick previews). */
    @GetMapping("/options")
    public List<OrderOptionResponse> listOptions(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(defaultValue = "10") int limit) {
        return service.listOptions(search, status, limit);
    }

    // === Commands ===

    /** Create a new order */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
        @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
        @Valid @RequestBody OrderCreateRequest req) {
        return service.create(idemKey, req);
    }

    /** Update order status (manual or system-level trigger) */
    @PatchMapping("/{id}/status")
    public OrderResponse setStatus(
        @PathVariable String id,
        @RequestParam OrderStatus status) {
        return service.updateStatus(id, status);
    }
}

