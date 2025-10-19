package com.github.ku4marez.catalog.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ku4marez.catalog.dto.DomainEvent;
import com.github.ku4marez.catalog.dto.ProductChangedData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductEventsPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final KafkaStarterProperties topics;

    public void productCreated(String productId) {
        send("product.created", productId);
    }
    public void productUpdated(String productId) {
        send("product.updated", productId);
    }
    public void productDeleted(String productId) {
        send("product.deleted", productId);
    }

    private void send(String type, String productId) {
        var evt = new DomainEvent<>(
            UUID.randomUUID().toString(),
            type,
            "catalog-service",
            Instant.now().toString(),
            1,
            new ProductChangedData(productId)
        );
        var json = toJson(evt);
        var rec = new ProducerRecord<String, String>(topics.getProductTopic(), productId, json);
        rec.headers()
            .add("eventType", type.getBytes(StandardCharsets.UTF_8))
            .add("schemaVersion", "1".getBytes(StandardCharsets.UTF_8));
        kafka.send(rec);
    }

    @SneakyThrows
    private String toJson(Object obj) { return new ObjectMapper().writeValueAsString(obj); }
}
