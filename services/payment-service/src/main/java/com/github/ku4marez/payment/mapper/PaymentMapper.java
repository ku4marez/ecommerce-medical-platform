package com.github.ku4marez.payment.mapper;

import com.github.ku4marez.payment.dto.PaymentResponse;
import com.github.ku4marez.payment.dto.RefundResponse;
import com.github.ku4marez.payment.entity.PaymentEntity;
import com.github.ku4marez.payment.entity.PaymentRefundEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {
    PaymentResponse toResponse(PaymentEntity e);
    RefundResponse toResponse(PaymentRefundEntity e);
}
