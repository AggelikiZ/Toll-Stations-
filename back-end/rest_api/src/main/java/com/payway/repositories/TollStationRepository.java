package com.payway.repositories;

import com.payway.models.TollStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface TollStationRepository extends JpaRepository<TollStation, String> {
    @Query(value = """
            SELECT
                ts.station_id AS stationId,
                ts.station_name AS stationName,
                ts.op_id AS operatorId,
                op.op_name AS operatorName,
                COUNT(p.pass_id) AS numberOfPasses,
                GROUP_CONCAT(
                    CONCAT(
                        '{"passId":', p.pass_id,
                        ',"passTime":"', p.pass_time,
                        '","tagRef":"', p.tag_ref,
                        '","charge":', p.charge, '}'
                    ) SEPARATOR ',') AS passDetails
            FROM
                TollStation ts
            LEFT JOIN Operator op ON ts.op_id = op.op_id
            LEFT JOIN Pass p ON ts.station_id = p.station_id
            WHERE
                ts.station_id = ?1
                AND p.pass_time BETWEEN ?2 AND ?3
            GROUP BY
                ts.station_id, ts.station_name, ts.op_id, op.op_name;
            """, nativeQuery = true)
    Map<String, Object> findTollStationPassesByIdAndTimeRange(
            String stationId,
            LocalDate startTime,
            LocalDate endTime
    );
}
