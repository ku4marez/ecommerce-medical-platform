package com.github.ku4marez.order.repository;

import com.github.ku4marez.order.entity.OrderEntity;
import com.github.ku4marez.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface OrderRepositoryCustom {

    Page<OrderEntity> search(String customerId,
                             OrderStatus status,
                             Instant from,
                             Instant to,
                             Pageable pageable);

    List<OrderEntity> findOptions(String search,
                                  OrderStatus status,
                                  int limit);
}
