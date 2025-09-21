package com.github.ku4marez.payment.configuration;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsConsumer {

    @KafkaListener(topics = "${ecom.kafka.order-topic}", groupId = "payments-service")
    public void onOrderEvent(String message) {
        // create PaymentIntent, update payment link, etc.
    }
}
