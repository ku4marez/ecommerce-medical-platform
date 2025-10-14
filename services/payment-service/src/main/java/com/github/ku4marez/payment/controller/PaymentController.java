package com.github.ku4marez.payment.controller;

import com.github.ku4marez.payment.dto.CreatePaymentRequest;
import com.github.ku4marez.payment.dto.PaymentResponse;
import com.github.ku4marez.payment.dto.RefundRequest;
import com.github.ku4marez.payment.dto.RefundResponse;
import com.github.ku4marez.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest req) {
        return service.create(req);
    }

    // Stripe webhook (will verify signature when you add Stripe)
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
