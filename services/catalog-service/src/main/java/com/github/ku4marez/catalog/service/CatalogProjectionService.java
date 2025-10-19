package com.github.ku4marez.catalog.service;

import com.github.ku4marez.catalog.dto.DomainEvent;
import com.github.ku4marez.catalog.dto.OrderCreatedData;
import com.github.ku4marez.catalog.entity.EventInboxEntity;
import com.github.ku4marez.catalog.repository.EventInboxRepository;
import com.github.ku4marez.catalog.repository.ProductStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CatalogProjectionService {
    private final ProductStatsRepository statsRepo;
    private final EventInboxRepository inbox;

    @Transactional
    public void onOrderCreated(DomainEvent<OrderCreatedData> evt) {
        if (inbox.existsByEventId(evt.eventId())) return;

        var when = Instant.parse(evt.occurredAt());
        for (var item : evt.data().items()) {
            statsRepo.upsertCounters(item.productId(), item.quantity(), when);
        }

        inbox.save(new EventInboxEntity(/* set eventId, type, receivedAt */));
    }
}


