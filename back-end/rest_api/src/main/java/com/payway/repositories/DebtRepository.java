package com.payway.repositories;

import com.payway.models.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt, String> {
    // Additional query methods can be defined here if needed
}
