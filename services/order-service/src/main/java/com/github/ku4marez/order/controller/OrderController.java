package com.github.ku4marez.order.controller;

import com.github.ku4marez.order.dto.OrderCreateRequest;
import com.github.ku4marez.order.dto.OrderResponse;
import com.github.ku4marez.order.entity.OrderStatus;
import com.github.ku4marez.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable String id) {
        return service.get(id);
    }

    @GetMapping
    public Page<OrderResponse> list(@RequestParam String customerId,
                                    @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
                                    Pageable pageable) {
        return service.listByCustomer(customerId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
                                @Valid @RequestBody OrderCreateRequest req) {
        return service.create(idemKey, req);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse setStatus(@PathVariable String id, @RequestParam OrderStatus status) {
        return service.updateStatus(id, status);
    }
}
