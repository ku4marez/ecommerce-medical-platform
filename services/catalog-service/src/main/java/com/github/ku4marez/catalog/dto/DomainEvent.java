package com.github.ku4marez.catalog.dto;

import java.io.Serializable;

public record DomainEvent<T>(String eventId,
                             String eventType,
                             String source,
                             String occurredAt,
                             int schemaVersion,
                             T data
) implements Serializable {
}
