package com.github.ku4marez.catalog.configuration;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CatalogOrderConsumer {

    @KafkaListener(topics = "${ecom.kafka.order-topic}", groupId = "catalog-service")
    public void onOrderEvent(String message) {
        // handle order-side effects if you need (often catalog doesn't consume orders)
    }
}
