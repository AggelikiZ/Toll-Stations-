package com.payway.controllers;

import com.payway.services.PassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/passAnalysis")
public class AnalysisController {

    private final PassService passService;

    public AnalysisController(PassService passService) {
        this.passService = passService;
    }

    @GetMapping("/{stationOpID}/{tagOpID}/{date_from}/{date_to}")
    public ResponseEntity<Map<String, Object>> getPassAnalysis(
            @PathVariable String stationOpID,
            @PathVariable String tagOpID,
            @PathVariable String date_from,
            @PathVariable String date_to
    ) {
        try {
            // Format dates from "YYYYMMDD"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime startDate = LocalDate.parse(date_from, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(date_to, formatter).atTime(23, 59, 59);

            // Call service
            Map<String, Object> response = passService.getPassAnalysis(stationOpID, tagOpID, startDate, endDate);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }
}
