package com.payway.repositories;

import com.payway.models.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {

    @Query("SELECT p FROM Pass p WHERE p.stationId = :stationID AND p.tagRef = :tagID AND p.passTime BETWEEN :dateFrom AND :dateTo")
    List<Pass> findPassesByStationAndTagAndDateRange(String stationID, String tagID, LocalDateTime dateFrom, LocalDateTime dateTo);
}
