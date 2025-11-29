package com.github.ku4marez.inventory.mapper;

import com.github.ku4marez.inventory.dto.api.StockItemResponse;
import com.github.ku4marez.inventory.entity.StockItemEntity;

@Mapper
public interface StockItemMapper extends DefaultMapper {
    StockItemResponse toResponse(StockItemEntity e);
}
