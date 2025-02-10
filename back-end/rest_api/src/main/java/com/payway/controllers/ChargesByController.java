package com.payway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payway.services.ChargeService;
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
@RequestMapping("/api/chargesBy")
public class ChargesByController {
    private final ChargeService chargeService;
    private final ObjectMapper objectMapper;
    private final Json2CSV json2CSV;

    public ChargesByController(ChargeService chargeService) {
        this.chargeService = chargeService;
        this.objectMapper = new ObjectMapper();
        this.json2CSV = new Json2CSV();
    }

    @GetMapping(value = "/{tollOpID}/{date_from}/{date_to}", produces = "application/json")
    public ResponseEntity<?> getChargesBy(
            @PathVariable String tollOpID,
            @PathVariable String date_from,
            @PathVariable String date_to,
            @RequestParam(required = false, defaultValue = "json") String format
    ) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime startDate = LocalDate.parse(date_from, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(date_to, formatter).atTime(23, 59, 59);

            Object response = chargeService.getChargesBy(tollOpID, date_from, date_to, format);

            if ("csv".equalsIgnoreCase(format) && response instanceof String) { // Ελέγχουμε αν είναι ήδη CSV
                return ResponseEntity.ok()
                        .header("Content-Disposition", "inline; filename=charges_by.csv")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(response); // Σωστό content-type
            }

            if (response == null || (response instanceof List && ((List<?>) response).isEmpty())) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(response);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}
