package com.github.ku4marez.inventory.mapper.impl;


import com.github.ku4marez.inventory.dto.api.ReservationResponse;
import com.github.ku4marez.inventory.entity.ReservationEntity;
import com.github.ku4marez.inventory.mapper.ReservationMapper;

public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationResponse toResponse(ReservationEntity e) {
        if (e == null) return null;

        return new ReservationResponse(
            e.getId(),
            e.getProductId(),
            e.getOrderId(),
            e.getQuantity(),
            e.getStatus(),
            e.getExpiresAt(),
            this.map(e.getCreationDate()),
            this.map(e.getUpdatedDate())
        );
    }
}
