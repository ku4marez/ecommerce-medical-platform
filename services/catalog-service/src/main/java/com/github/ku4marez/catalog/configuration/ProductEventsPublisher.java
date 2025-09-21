package com.github.ku4marez.catalog.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

@Service
@RequiredArgsConstructor
public class ProductEventsPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final KafkaStarterProperties topics;

    public void publishProductUpdated(String productId, String payloadJsonOrText) {
        kafka.send(topics.getProductTopic(), productId, payloadJsonOrText);
    }
}
