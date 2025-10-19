package com.github.ku4marez.inventory.configuration;

import com.github.ku4marez.inventory.entity.StockItemEntity;
import com.github.ku4marez.inventory.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventsConsumer {
    private final StockItemRepository stock;

    @KafkaListener(topics = "${ecom.kafka.product-topic}", groupId = "inventory-service")
    public void onProductEvent(Map<String, Object> msg) {
        var type = (String) msg.get("eventType");
        var productId = (String) msg.get("productId");
        log.info("Received product event {} for {}", type, productId);

        switch (type) {
            case "product.created" -> {
                if (stock.findByProductId(productId).isEmpty()) {
                    var s = new StockItemEntity();
                    s.setProductId(productId);
                    s.setAvailable(0);
                    s.setReserved(0);
                    stock.save(s);
                }
            }
            case "product.archived" -> {
                stock.findByProductId(productId).ifPresent(si -> {
                    si.setAvailable(0);
                    stock.save(si);
                });
            }
            default -> log.debug("No handler for {}", type);
        }
    }
}

