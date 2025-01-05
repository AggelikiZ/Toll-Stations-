package com.payway.controllers;

import com.payway.services.PassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/passAnalysis")
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
            // Format dates using the specified pattern
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(date_from, formatter);
            LocalDateTime endDate = LocalDateTime.parse(date_to, formatter);

            // Call the PassService to get the analysis
            Map<String, Object> response = passService.getPassAnalysis(stationOpID, tagOpID, startDate, endDate);

            // Return the response
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }
}
