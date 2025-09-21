package com.github.ku4marez.payment.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.github.ku4marez.ecom.starters.kafka.KafkaStarterProperties;

@Service
@RequiredArgsConstructor
public class PaymentEventsPublisher {
    private final KafkaTemplate<String, String> kafka;
    private final KafkaStarterProperties topics;

    public void publishPaymentSucceeded(String paymentId, String payload) {
        kafka.send(topics.getPaymentTopic(), paymentId, payload);
    }
}
