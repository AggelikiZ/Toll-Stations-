package com.payway.repositories;

import com.payway.models.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, String> {
    Optional<Operator> findByOpName(String opName);
    Optional<Operator> findByUserId(int userID);
}