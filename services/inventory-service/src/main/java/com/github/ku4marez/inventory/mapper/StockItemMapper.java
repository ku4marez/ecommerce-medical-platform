package com.github.ku4marez.inventory.mapper;

import com.github.ku4marez.inventory.dto.StockItemResponse;
import com.github.ku4marez.inventory.entity.StockItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StockItemMapper {
    StockItemResponse toResponse(StockItemEntity e);

    @SuppressWarnings("unused")
    default Instant map(LocalDateTime value) {
        return value != null ? value.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    @SuppressWarnings("unused")
    default LocalDateTime map(Instant value) {
        return value != null ? LocalDateTime.ofInstant(value, ZoneId.systemDefault()) : null;
    }
}
