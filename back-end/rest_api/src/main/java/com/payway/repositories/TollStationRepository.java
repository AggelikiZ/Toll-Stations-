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
                ?2 as periodFrom,
                ?3 as periodTo,
                ?4 as requestTimestamp,
                COUNT(p.pass_id) AS nPasses,
                GROUP_CONCAT(
                    CONCAT(
                        '{"passIndex":', row_num,\s
                        ',"passID":', p.pass_id,
                        ',"timestamp":"', p.pass_time,
                        ',","tagID":"', p.tag_ref,
                        ',"tagProvider":"', t.op_id,
                        '","passCharge":', p.charge,
                        ',"passType":"',\s
                        CASE\s
                            WHEN t.op_id = ts.op_id THEN 'home'
                            ELSE 'visitor'
                        END,  -- "home" or "visitor" based on provider match
                        '"}'
                    ) SEPARATOR ',') AS passDetails
            FROM
                TollStation ts
            LEFT JOIN Operator op ON ts.op_id = op.op_id
            LEFT JOIN Pass p ON ts.station_id = p.station_id
            LEFT JOIN Tag t ON p.tag_ref = t.tag_ref
            JOIN (
                SELECT\s
                    p.pass_id,
                    p.station_id,
                    p.pass_time,
                    p.tag_ref,
                    p.charge,
                    ROW_NUMBER() OVER (PARTITION BY p.station_id ORDER BY p.pass_time) AS row_num
                FROM Pass p
            ) AS numbered_passes
            ON p.pass_id = numbered_passes.pass_id
            WHERE
                ts.station_id = ?1
                AND p.pass_time BETWEEN ?2 AND ?3
            GROUP BY
                ts.station_id, ts.station_name, ts.op_id, op.op_name;
            """, nativeQuery = true)
    Map<String, Object> findTollStationPassesByIdAndTimeRange(
            String stationId,
            LocalDate startTime,
            LocalDate endTime,
            LocalDateTime currentTimestamp
    );

    List<TollStation> findByOpId(String opId);
}
