package com.github.ku4marez.payment.controller;

import com.github.ku4marez.payment.dto.*;
import com.github.ku4marez.payment.entity.PaymentProvider;
import com.github.ku4marez.payment.entity.PaymentStatus;
import com.github.ku4marez.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // --- Queries ---
    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable String id) {
        return service.get(id);
    }

    @GetMapping
    public Page<PaymentResponse> list(
        @RequestParam(required = false) String orderId,
        @RequestParam(required = false) PaymentStatus status,
        @RequestParam(required = false) PaymentProvider provider,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
        Pageable pageable) {
        return service.list(orderId, status, provider, from, to, pageable);
    }

    @GetMapping("/options")
    public List<PaymentOptionResponse> options(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) PaymentStatus status,
        @RequestParam(defaultValue = "10") int limit) {
        return service.listOptions(search, status, limit);
    }

    // --- Commands ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest req) {
        return service.create(req);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody String payload,
                                        @RequestHeader(value = "Stripe-Signature", required = false) String sig) {
        service.onProviderEvent(payload, sig);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refunds")
    public RefundResponse refund(@Valid @RequestBody RefundRequest req) {
        return service.refund(req);
    }
}
