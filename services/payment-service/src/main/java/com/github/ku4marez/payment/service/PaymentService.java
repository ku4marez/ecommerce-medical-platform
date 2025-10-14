package com.github.ku4marez.payment.service;

import com.github.ku4marez.payment.dto.CreatePaymentRequest;
import com.github.ku4marez.payment.dto.PaymentResponse;
import com.github.ku4marez.payment.dto.RefundRequest;
import com.github.ku4marez.payment.dto.RefundResponse;
import com.github.ku4marez.payment.entity.*;
import com.github.ku4marez.payment.mapper.PaymentMapper;
import com.github.ku4marez.payment.repository.PaymentRefundRepository;
import com.github.ku4marez.payment.repository.PaymentRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository payments;
    private final PaymentRefundRepository refunds;
    private final PaymentMapper mapper;
    // private final PaymentEventsPublisher publisher; // wire later with Kafka

    public PaymentResponse get(String id) {
        return mapper.toResponse(payments.findById(id).orElseThrow());
    }

    /** Create pending payment link/intent for the order (idempotent by orderId). */
    @Transactional
    public PaymentResponse create(CreatePaymentRequest req) {
        var existing = payments.findByOrderId(req.orderId());
        if (existing.isPresent()) return mapper.toResponse(existing.get());

        var p = new PaymentEntity();
        p.setOrderId(req.orderId());
        p.setProvider(PaymentProvider.STRIPE);
        p.setStatus(PaymentStatus.PENDING);

        // TODO (Stripe real): create PaymentIntent or Checkout Session with stripe-java
        // and set:
        //   p.setProviderRef(intentOrSessionId);
        //   p.setCheckoutUrl(checkoutUrlIfSession);

        var saved = payments.save(p);
        return mapper.toResponse(saved);
    }

    /** Provider webhook handler (set SUCCEEDED/FAILED). */
    @Transactional
    public void onProviderEvent(String payload, @Nullable String signatureHeader) {
        // TODO (Stripe real): verify signature with webhook secret, parse event
        // - locate PaymentEntity by providerRef
        // - set status SUCCEEDED/FAILED
        // - save + publish payment.succeeded/payment.failed (later)
    }

    /** Manual refund helper. */
    @Transactional
    public RefundResponse refund(RefundRequest req) {
        var payment = payments.findById(req.paymentId()).orElseThrow();

        var r = new PaymentRefundEntity();
        r.setPaymentId(payment.getId());
        r.setProvider(payment.getProvider());
        r.setStatus(RefundStatus.PENDING);
        r.setAmount(req.amount());
        r.setCurrency("usd"); // or from payment/order; adjust as needed
        r.setReason(req.reason());

        // TODO (Stripe real): call Refund.create -> set providerRefundRef and final status
        var saved = refunds.save(r);
        return mapper.toResponse(saved);
    }
}
