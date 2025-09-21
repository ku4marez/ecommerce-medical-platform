package com.github.ku4marez.order.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

@Service
@RequiredArgsConstructor
public class OrderEventsPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final KafkaStarterProperties topics;

    public void publishOrderCreated(String orderId, String payload) {
        kafka.send(topics.getOrderTopic(), orderId, payload);
    }
}
