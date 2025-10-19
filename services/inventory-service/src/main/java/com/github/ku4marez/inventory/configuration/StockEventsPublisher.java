package com.github.ku4marez.inventory.configuration;

import com.github.ku4marez.inventory.entity.ReservationEntity;
import com.github.ku4marez.inventory.entity.StockItemEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventsPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${ecom.kafka.stock-topic}") private String topic;

    public void stockReserved(ReservationEntity r) {
        send("stock.reserved", Map.of(
            "productId", r.getProductId(),
            "orderId", r.getOrderId(),
            "quantity", r.getQuantity(),
            "status", r.getStatus().name()
        ));
    }

    public void stockReleased(ReservationEntity r) {
        send("stock.released", Map.of(
            "productId", r.getProductId(),
            "orderId", r.getOrderId(),
            "quantity", r.getQuantity(),
            "status", r.getStatus().name()
        ));
    }

    public void stockConfirmed(ReservationEntity r) {
        send("stock.confirmed", Map.of(
            "productId", r.getProductId(),
            "orderId", r.getOrderId(),
            "quantity", r.getQuantity(),
            "status", r.getStatus().name()
        ));
    }

    public void stockAdjusted(StockItemEntity s, String reason) {
        send("stock.adjusted", Map.of(
            "productId", s.getProductId(),
            "available", s.getAvailable(),
            "reserved", s.getReserved(),
            "reason", reason
        ));
    }

    private void send(String type, Map<String, Object> payload) {
        payload = new HashMap<>(payload);
        payload.put("eventType", type);
        payload.put("timestamp", Instant.now().toString());
        kafkaTemplate.send(topic, (String) payload.get("productId"), payload);
        log.info("Published {} event: {}", type, payload);
    }
}
