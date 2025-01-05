package com.payway.repositories;

import com.payway.models.TollStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.payway.models.TollStation;


import java.time.LocalDate;
;import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TollStationRepository extends JpaRepository<TollStation, String> {

    @Query("SELECT ts.id AS stationId, COUNT(p.passId) AS numberOfPasses " +
            "FROM TollStation ts LEFT JOIN Pass p ON ts.id = p.stationId " +
            "WHERE ts.id = :stationId AND p.passTime BETWEEN :startTime AND :endTime " +
            "GROUP BY ts.id")
    Map<String, Object> TollStationPassesByIdAndTimeRange(
            @Param("stationId") String stationId,
            @Param("startTime") LocalDate startTime,
            @Param("endTime") LocalDate endTime);

    @Query(value = """
            SELECT
                ts.station_id AS stationID,
                op.op_name AS stationOperator,
                ?2 AS periodFrom,
                ?3 AS periodTo,
                ?4 AS requestTimestamp,
                COUNT(p.pass_id) OVER (PARTITION BY ts.station_id) AS nPasses,
                ROW_NUMBER() OVER (PARTITION BY ts.station_id ORDER BY p.pass_time) AS passIndex,
                p.pass_id AS passID,
                p.pass_time AS timestamp,
                p.tag_ref AS tagID,
                t.op_id AS tagProvider,
                p.charge AS passCharge,
                CASE
                    WHEN t.op_id = ts.op_id THEN 'home'
                    ELSE 'visitor'
                END AS passType
            FROM
                TollStation ts
            LEFT JOIN Operator op ON ts.op_id = op.op_id
            LEFT JOIN Pass p ON ts.station_id = p.station_id
            LEFT JOIN Tag t ON p.tag_ref = t.tag_ref
            WHERE
                ts.station_id = ?1
                AND p.pass_time BETWEEN ?2 AND ?3
            ORDER BY
                p.pass_time;
            """, nativeQuery = true)
    List<Map<String, Object>> findTollStationPassesByIdAndTimeRange(
            String stationId,
            LocalDate startTime,
            LocalDate endTime,
            LocalDateTime currentTimestamp
    );

    List<TollStation> findByOpId(String opId);
}
