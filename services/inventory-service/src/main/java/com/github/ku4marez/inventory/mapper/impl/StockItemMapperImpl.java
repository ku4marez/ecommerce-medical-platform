package com.github.ku4marez.inventory.mapper.impl;

import com.github.ku4marez.inventory.dto.api.StockItemResponse;
import com.github.ku4marez.inventory.entity.StockItemEntity;
import com.github.ku4marez.inventory.mapper.StockItemMapper;

public class StockItemMapperImpl implements StockItemMapper {

    @Override
    public StockItemResponse toResponse(StockItemEntity e) {
        if (e == null) return null;

        return new StockItemResponse(
            e.getProductId(),
            e.getAvailable(),
            e.getReserved(),
            this.map(e.getCreationDate()),
            this.map(e.getUpdatedDate())
        );
    }
}
