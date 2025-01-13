package com.payway.controllers;

import com.payway.services.ChargeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/chargesBy")
public class ChargesByController {
    private final ChargeService chargeService;

    // Ο constructor τώρα ταιριάζει με το όνομα της κλάσης
    public ChargesByController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @GetMapping("/{tollOpID}/{date_from}/{date_to}")
    public ResponseEntity<Map<String, Object>> getChargesBy(
            @PathVariable String tollOpID,
            @PathVariable String date_from,
            @PathVariable String date_to
    ) {
        try {
            // Format dates from "YYYYMMDD"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime startDate = LocalDate.parse(date_from, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(date_to, formatter).atTime(23, 59, 59);

            // Call service
            Map<String, Object> response = chargeService.getChargesBy(tollOpID, startDate, endDate);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }
}

