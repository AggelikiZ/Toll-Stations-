package com.payway.repositories;

import com.payway.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByFromOpIdAndToOpId(String fromOpId, String toOpId);

    @Query("SELECT new map(p.toOpId as toOpId, o.opName as toOpName, p.amount as amount, p.date as date, p.details as details) " +
            "FROM Payment p JOIN Operator o ON p.toOpId = o.opId " +
            "WHERE p.fromOpId = :fromOpId")
    List<Map<String, Object>> findPaymentsByFromOpId(String fromOpId);

    @Query("SELECT new map(p.fromOpId as fromOpId, o.opName as toOpName, p.amount as amount, p.date as date, p.details as details) " +
            "FROM Payment p JOIN Operator o ON p.fromOpId = o.opId " +
            "WHERE p.toOpId = :toOpId")
    List<Map<String, Object>> findPaymentsByToOpId(String toOpId);


}
