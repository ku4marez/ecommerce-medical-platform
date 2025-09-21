package com.github.ku4marez.inventory.configuration;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsConsumer {

    @KafkaListener(topics = "${ecom.kafka.product-topic}", groupId = "inventory-service")
    public void onProductEvent(String message) {
        // update local product cache / projections if you keep one
    }
}
