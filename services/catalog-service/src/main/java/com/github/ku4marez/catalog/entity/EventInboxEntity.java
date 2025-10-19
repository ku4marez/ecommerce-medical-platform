package com.github.ku4marez.catalog.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document("event_inbox") // keeps track of handled events
@CompoundIndexes({
    @CompoundIndex(name="ix_event_inbox_eventId", def="{'eventId':1}", unique = true)
})
public class EventInboxEntity {
    @Id
    private String id;
    private String eventId;
    private String eventType;
    private Instant receivedAt;
}

