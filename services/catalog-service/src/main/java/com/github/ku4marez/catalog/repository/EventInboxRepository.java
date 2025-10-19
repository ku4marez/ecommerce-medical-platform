package com.github.ku4marez.catalog.repository;

import com.github.ku4marez.catalog.entity.EventInboxEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventInboxRepository extends MongoRepository<EventInboxEntity, String> {
    boolean existsByEventId(String eventId);
}
