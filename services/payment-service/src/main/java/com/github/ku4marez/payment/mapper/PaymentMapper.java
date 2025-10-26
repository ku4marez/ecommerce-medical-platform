package com.github.ku4marez.payment.mapper;

import com.github.ku4marez.payment.dto.PaymentResponse;
import com.github.ku4marez.payment.dto.RefundResponse;
import com.github.ku4marez.payment.entity.PaymentEntity;
import com.github.ku4marez.payment.entity.PaymentRefundEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {
    PaymentResponse toResponse(PaymentEntity e);
    RefundResponse toResponse(PaymentRefundEntity e);

    @SuppressWarnings("unused")
    default Instant map(LocalDateTime value) {
        return value != null ? value.atZone(ZoneId.systemDefault()).toInstant() : null;
    }

    @SuppressWarnings("unused")
    default LocalDateTime map(Instant value) {
        return value != null ? LocalDateTime.ofInstant(value, ZoneId.systemDefault()) : null;
    }
}
