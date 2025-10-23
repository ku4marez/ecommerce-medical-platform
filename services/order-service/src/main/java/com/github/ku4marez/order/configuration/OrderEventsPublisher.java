package com.github.ku4marez.order.configuration;

import com.github.ku4marez.order.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventsPublisher {
    private final KafkaTemplate<String, Object> kafka;
    private final KafkaStarterProperties topics;

    public void publishOrderCreated(OrderEntity e) {
        send("order.created", Map.of(
            "orderId", e.getId(),
            "customerId", e.getCustomerId(),
            "items", e.getItems(),
            "total", e.getTotalAmount(),
            "status", e.getStatus().name(),
            "ttlSeconds", 600
//            "idempotencyKey", e.getIdempotencyKey()
        ));
    }

    public void publishOrderCancelled(OrderEntity e, String reason) {
        send("order.cancelled", Map.of(
            "orderId", e.getId(),
            "reason", reason
        ));
    }

    public void publishOrderConfirmed(OrderEntity e) {
        send("order.confirmed", Map.of(
            "orderId", e.getId(),
            "status", e.getStatus().name()
        ));
    }

    private void send(String type, Map<String, Object> payload) {
        payload = new HashMap<>(payload);
        payload.put("eventType", type);
        payload.put("timestamp", Instant.now().toString());
        kafka.send(topics.getOrderTopic(), (String) payload.get("orderId"), payload);
        log.info("Published {} event: {}", type, payload);
    }
}
