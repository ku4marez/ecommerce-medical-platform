package com.github.ku4marez.payment.repository;

import com.github.ku4marez.payment.entity.PaymentRefundEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRefundRepository extends MongoRepository<PaymentRefundEntity, String> {
    List<PaymentRefundEntity> findByPaymentId(String paymentId);
}
