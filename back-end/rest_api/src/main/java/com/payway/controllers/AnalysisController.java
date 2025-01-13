package com.payway.controllers;

import com.payway.services.PassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/passAnalysis")
public class AnalysisController {

    private final PassService passService;

    public AnalysisController(PassService passService) {
        this.passService = passService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }

    }
}
