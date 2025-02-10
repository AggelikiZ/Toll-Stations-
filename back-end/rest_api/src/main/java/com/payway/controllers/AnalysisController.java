package com.payway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payway.models.Operator;
import com.payway.models.TollStation;
import com.payway.models.User;
import com.payway.repositories.UserRepository;
import com.payway.services.PassService;
import com.payway.utils.Json2CSV;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/passAnalysis")
public class AnalysisController {

    private final PassService passService;
    private final ObjectMapper objectMapper;
    private final Json2CSV json2CSV;

    public AnalysisController(PassService passService) {
        this.passService = passService;
        this.objectMapper = new ObjectMapper();
        this.json2CSV = new Json2CSV();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/{stationOpID}/{tagOpID}/{date_from}/{date_to}", produces = "application/json")
    public ResponseEntity<?> getPassAnalysis(
            @PathVariable String stationOpID,
            @PathVariable String tagOpID,
            @PathVariable String date_from,
            @PathVariable String date_to,
            @RequestParam(required = false, defaultValue = "json") String format
    ) {
        try {
            // Format dates from "YYYYMMDD"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime startDate = LocalDate.parse(date_from, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(date_to, formatter).atTime(23, 59, 59);

            // Call service
            Object response = passService.getPassAnalysis(stationOpID, tagOpID, startDate, endDate, format);

            if ("csv".equalsIgnoreCase(format)) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "inline; filename=pass_analysis.csv")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(response);
            }

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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/operators")
    public ResponseEntity<List<Operator>> getAllOperators() {
        return ResponseEntity.ok(passService.getAllOperators());
    }
}
