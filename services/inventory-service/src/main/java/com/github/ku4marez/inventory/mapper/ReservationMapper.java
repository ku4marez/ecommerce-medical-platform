package com.github.ku4marez.inventory.mapper;

import com.github.ku4marez.inventory.dto.api.ReservationResponse;
import com.github.ku4marez.inventory.entity.ReservationEntity;

@Mapper
public interface ReservationMapper extends DefaultMapper {
    ReservationResponse toResponse(ReservationEntity e);
}
