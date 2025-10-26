package com.github.ku4marez.inventory.mapper;

import com.github.ku4marez.inventory.dto.ReservationResponse;
import com.github.ku4marez.inventory.dto.ReserveRequest;
import com.github.ku4marez.inventory.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ReservationEntity toNewEntity(ReserveRequest req);

    ReservationResponse toResponse(ReservationEntity e);

    @SuppressWarnings("unused")
    default Instant map(LocalDateTime value) {
        return value != null ? value.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    @SuppressWarnings("unused")
    default LocalDateTime map(Instant value) {
        return value != null ? LocalDateTime.ofInstant(value, ZoneId.systemDefault()) : null;
    }
}
