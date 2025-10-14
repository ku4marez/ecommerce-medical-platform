package com.github.ku4marez.inventory.mapper;

import com.github.ku4marez.inventory.dto.StockItemResponse;
import com.github.ku4marez.inventory.entity.StockItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StockItemMapper {
    StockItemResponse toResponse(StockItemEntity e);
}
