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
public interface TollStationRepository extends JpaRepository<TollStation, String> {

    @Query("SELECT ts.id AS stationId, COUNT(p.passId) AS numberOfPasses " +
            "FROM TollStation ts LEFT JOIN Pass p ON ts.id = p.stationId " +
            "WHERE ts.id = :stationId AND p.passTime BETWEEN :startTime AND :endTime " +
            "GROUP BY ts.id")
    Map<String, Object> findTollStationPassesByIdAndTimeRange(
            @Param("stationId") String stationId,
            @Param("startTime") LocalDate startTime,
            @Param("endTime") LocalDate endTime);

}
