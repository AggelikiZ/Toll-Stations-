package com.payway.repositories;

import com.payway.models.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Debt.DebtId> {
    Optional<Debt> findByFromOpIdAndToOpId(String fromOpId, String toOpId);
}
