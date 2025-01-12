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

    @Query(value = """

            SELECT\s
    ts.op_id AS tollOpID,
    t.op_id AS tagOpID,
    :startTime AS periodFrom,
    :endTime AS periodTo,
    CURRENT_TIMESTAMP AS requestTimestamp,
    COUNT(p.pass_id) AS nPasses,
    SUM(p.charge) AS totalCost
FROM\s
    Pass p
JOIN\s
    TollStation ts ON p.station_id = ts.station_id
JOIN\s
    Tag t ON p.tag_ref = t.tag_ref
WHERE\s
    ts.op_id = :tollOpID\s
    AND t.op_id = :tagOpID
    AND p.pass_time BETWEEN :startTime AND :endTime
GROUP BY\s
    ts.op_id, t.op_id;


           
""", nativeQuery = true)
    Map<String, Object> passesCost(
            @Param("tollOpID") String tollOpID,
            @Param("tagOpID") String tagOpID,
            @Param("startTime") LocalDate startTime,
            @Param("endTime") LocalDate endTime);





    @Query(value = """
    SELECT p.* 
    FROM pass p
    JOIN tag t ON p.tag_ref = t.tag_ref
    WHERE p.station_id IN :stationIds
      AND p.pass_time BETWEEN :dateFrom AND :dateTo
      AND t.op_id != :tollOpID
""", nativeQuery = true)
    List<Pass> findPassesByStationIdsAndDateRangeAndVisitingOperator(
            @Param("stationIds") Set<String> stationIds,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("tollOpID") String tollOpID
    );



    List<Pass> findByStationIdInAndPassTimeBetween(Set<String> stationIds, LocalDateTime startDate, LocalDateTime endDate);
}
