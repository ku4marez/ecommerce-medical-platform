package com.github.ku4marez.inventory.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

@Service
@RequiredArgsConstructor
public class StockEventsPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final KafkaStarterProperties topics;

    public void publishStockReserved(String reservationId, String payload) {
        kafka.send(topics.getStockTopic(), reservationId, payload);
    }
}
