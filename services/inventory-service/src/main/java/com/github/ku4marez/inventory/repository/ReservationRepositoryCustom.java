package com.github.ku4marez.inventory.repository;

import com.github.ku4marez.inventory.entity.ReservationEntity;
import com.github.ku4marez.inventory.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {
    Page<ReservationEntity> search(String productId, String orderId, ReservationStatus status, Pageable pageable);
}
