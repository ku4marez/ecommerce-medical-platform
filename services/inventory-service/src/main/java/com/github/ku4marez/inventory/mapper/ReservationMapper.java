package com.github.ku4marez.inventory.mapper;

import com.github.ku4marez.inventory.dto.ReservationResponse;
import com.github.ku4marez.inventory.dto.ReserveRequest;
import com.github.ku4marez.inventory.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationMapper {

    // New reservation from request; service will set status/expiry if missing
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "expiresAt", ignore = true) // service computes from ttlSeconds
    ReservationEntity toNewEntity(ReserveRequest req);

    ReservationResponse toResponse(ReservationEntity e);
}
