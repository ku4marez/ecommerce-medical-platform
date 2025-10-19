package com.github.ku4marez.payment.configuration;

import com.github.ku4marez.payment.entity.PaymentEntity;
import com.github.ku4marez.payment.entity.PaymentRefundEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventsPublisher {
    private final KafkaTemplate<String, Object> kafka;
    private final KafkaStarterProperties topics;

    private void send(String type, Map<String, Object> payload) {
        payload.put("eventType", type);
        payload.put("timestamp", Instant.now().toString());
        kafka.send(topics.getPaymentTopic(), (String) payload.get("paymentId"), payload);
        log.info("Published {} event: {}", type, payload);
    }

    public void publishPaymentSucceeded(PaymentEntity p) {
        send("payment.succeeded", Map.of(
            "paymentId", p.getId(),
            "orderId", p.getOrderId(),
            "status", p.getStatus().name()
        ));
    }

    public void publishPaymentFailed(PaymentEntity p) {
        send("payment.failed", Map.of(
            "paymentId", p.getId(),
            "orderId", p.getOrderId(),
            "status", p.getStatus().name()
        ));
    }

    public void publishPaymentRefunded(PaymentEntity p, PaymentRefundEntity r) {
        send("payment.refunded", Map.of(
            "paymentId", p.getId(),
            "orderId", p.getOrderId(),
            "refundId", r.getId(),
            "amount", r.getAmount(),
            "status", r.getStatus().name()
        ));
    }
}
