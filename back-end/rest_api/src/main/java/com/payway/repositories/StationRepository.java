package com.payway.repositories;

import com.payway.models.TollStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.payway.models.TollStation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface StationRepository extends JpaRepository<TollStation, Long> {
}