package com.github.ku4marez.payment.service;

import com.github.ku4marez.payment.configuration.PaymentEventsPublisher;
import com.github.ku4marez.payment.dto.*;
import com.github.ku4marez.payment.entity.*;
import com.github.ku4marez.payment.exception.PaymentGatewayFailed;
import com.github.ku4marez.payment.mapper.PaymentMapper;
import com.github.ku4marez.payment.repository.PaymentRefundRepository;
import com.github.ku4marez.payment.repository.PaymentRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository payments;
    private final PaymentRefundRepository refunds;
    private final PaymentMapper mapper;
    private final PaymentEventsPublisher publisher;
    private final PaymentGatewayClient paymentGatewayClient;

    // ---------- Queries ----------
    public PaymentResponse get(String id) {
        return mapper.toResponse(payments.findById(id).orElseThrow());
    }

    public Page<PaymentResponse> list(String orderId, PaymentStatus status,
                                      PaymentProvider provider, Instant from, Instant to,
                                      Pageable pageable) {
        return payments.search(orderId, status, provider, from, to, pageable)
            .map(mapper::toResponse);
    }

    public List<PaymentOptionResponse> listOptions(String search, PaymentStatus status, int limit) {
        return payments.findOptions(search, status, limit).stream()
            .map(e -> new PaymentOptionResponse(e.getId(), e.getOrderId(), e.getStatus(), e.getProvider()))
            .toList();
    }

    // ---------- Commands ----------
    @Transactional
    public PaymentResponse create(CreatePaymentRequest req) {
        var existing = payments.findByOrderId(req.orderId());
        if (existing.isPresent()) return mapper.toResponse(existing.get());

        var p = new PaymentEntity();
        p.setOrderId(req.orderId());
        p.setProvider(PaymentProvider.STRIPE);
        p.setStatus(PaymentStatus.PENDING);

        try {
            var external = paymentGatewayClient
                .createPayment(req.orderId(), req.amount())
                .get();

            p.setProviderRef(external.providerRef());
            p.setCheckoutUrl(external.checkoutUrl());

        } catch (Exception e) {
            throw new PaymentGatewayFailed();
        }

        return mapper.toResponse(payments.save(p));
    }

    /** Webhook handler for Stripe (or another provider). */
    @Transactional
    public void onProviderEvent(String payload, @Nullable String signatureHeader) {
        // Example: pseudo handler for success/failure parsed from payload
        var providerRef = parseProviderRef(payload);
        var status = parseStatus(payload);

        var payment = payments.findByProviderRef(providerRef).orElseThrow();

        payment.setStatus(status);
        payments.save(payment);

        // Publish event to Orders service
        if (status == PaymentStatus.SUCCEEDED) {
            publisher.publishPaymentSucceeded(payment);
        } else if (status == PaymentStatus.FAILED) {
            publisher.publishPaymentFailed(payment);
        }
    }

    @Transactional
    public RefundResponse refund(RefundRequest req) {
        var payment = payments.findById(req.paymentId()).orElseThrow();

        var r = new PaymentRefundEntity();
        r.setPaymentId(payment.getId());
        r.setProvider(payment.getProvider());
        r.setStatus(RefundStatus.PENDING);
        r.setAmount(req.amount());
        r.setCurrency("usd");
        r.setReason(req.reason());

        var saved = refunds.save(r);

        publisher.publishPaymentRefunded(payment, saved);
        return mapper.toResponse(saved);
    }

    // Mock helpers for Stripe placeholders
    private String parseProviderRef(String payload) { return "mock_ref"; }
    private PaymentStatus parseStatus(String payload) { return PaymentStatus.SUCCEEDED; }
}

