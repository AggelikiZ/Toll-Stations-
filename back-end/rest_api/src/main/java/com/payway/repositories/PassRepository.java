package com.payway.repositories;

import com.payway.models.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {

    @Query("SELECT p FROM Pass p WHERE p.stationId = :stationID AND p.tagRef = :tagID AND p.passTime BETWEEN :dateFrom AND :dateTo")
    List<Pass> findPassesByStationAndTagAndDateRange(
            @Param("stationID") String stationID,
            @Param("tagID") String tagID,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo);


    @Query(value="""
        SELECT p FROM Pass p


""", nativeQuery = true)
    Map<String, Object> getPassesCost(
            String tollOpID,
            String tagOpID,
            LocalDate startTime,
            LocalDate endTime
    );

    List<Pass> findByStationIdInAndPassTimeBetween(Set<String> stationIds, LocalDateTime startDate, LocalDateTime endDate);
}
