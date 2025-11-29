package com.github.ku4marez.inventory.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface DefaultMapper {

    @SuppressWarnings("unused")
    default Instant map(LocalDateTime value) {
        return value != null ? value.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    @SuppressWarnings("unused")
    default LocalDateTime map(Instant value) {
        return value != null ? LocalDateTime.ofInstant(value, ZoneId.systemDefault()) : null;
    }
}
