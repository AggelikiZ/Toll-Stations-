package com.payway.repositories;

import com.payway.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByFromOpIdAndToOpId(String fromOpId, String toOpId);
}
