package com.github.ku4marez.catalog.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ku4marez.catalog.dto.DomainEvent;
import com.github.ku4marez.catalog.dto.OrderCreatedData;
import com.github.ku4marez.catalog.service.CatalogProjectionService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CatalogOrderConsumer {
    private final CatalogProjectionService projections;
    private final ObjectMapper om = new ObjectMapper();

    @KafkaListener(topics = "${ecom.kafka.order-topic}", groupId = "catalog-service")
    public void onOrderEvent(ConsumerRecord<String, String> rec) throws Exception {
        String eventType = header(rec, "eventType");
        String payload = rec.value();

        if ("order.created".equals(eventType)) {
            var evt = om.readValue(payload, new TypeReference<DomainEvent<OrderCreatedData>>() {});
            projections.onOrderCreated(evt);
        }

        // ignore other order events for catalog
    }

    private String header(ConsumerRecord<String,String> rec, String name) {
        var h = rec.headers().lastHeader(name);
        return h == null ? null : new String(h.value(), StandardCharsets.UTF_8);
    }
}
