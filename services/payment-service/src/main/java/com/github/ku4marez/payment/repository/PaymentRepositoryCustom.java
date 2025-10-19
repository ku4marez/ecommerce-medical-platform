package com.github.ku4marez.payment.repository;

import com.github.ku4marez.payment.entity.PaymentEntity;
import com.github.ku4marez.payment.entity.PaymentProvider;
import com.github.ku4marez.payment.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface PaymentRepositoryCustom {
    Page<PaymentEntity> search(String orderId,
                               PaymentStatus status,
                               PaymentProvider provider,
                               Instant from,
                               Instant to,
                               Pageable pageable);

    List<PaymentEntity> findOptions(String search, PaymentStatus status, int limit);
}

