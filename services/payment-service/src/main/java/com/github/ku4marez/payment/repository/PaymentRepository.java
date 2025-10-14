package com.github.ku4marez.payment.repository;

import com.github.ku4marez.payment.entity.PaymentEntity;
import com.github.ku4marez.payment.entity.PaymentProvider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByOrderId(String orderId);
    Optional<PaymentEntity> findByProviderAndProviderRef(PaymentProvider provider, String providerRef);
}
