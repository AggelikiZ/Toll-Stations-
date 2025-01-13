package com.payway.repositories;

import com.payway.models.Debt;
import com.payway.models.DebtDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Debt.DebtId> {
    Optional<Debt> findByFromOpIdAndToOpId(String fromOpId, String toOpId);

    @Query("SELECT new com.payway.models.DebtDetails(d.fromOpId, o.opName, d.debtAmount) " +
            "FROM Debt d JOIN Operator o ON d.fromOpId = o.opId " +
            "WHERE d.toOpId = :toOpId")
    List<DebtDetails> findDebtsToOperator(String toOpId);

    @Query("SELECT new com.payway.models.DebtDetails(d.toOpId, o.opName, d.debtAmount) " +
            "FROM Debt d JOIN Operator o ON d.toOpId = o.opId " +
            "WHERE d.fromOpId = :fromOpId")
    List<DebtDetails> findDebtsFromOperator(String fromOpId);
}
