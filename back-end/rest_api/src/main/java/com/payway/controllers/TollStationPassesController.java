package com.payway.controllers;

import com.payway.models.Generic500Response;
import com.payway.models.ResetStations200Response;
import com.payway.models.ResetStations400Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.payway.services.TollStationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api")
public class TollStationPassesController {

    private final TollStationService tollStationService;

    @Autowired
    public TollStationPassesController(TollStationService tollStationService) {
        this.tollStationService = tollStationService;
    }

    @GetMapping(value = "/tollStationPasses/{tollStationID}/{date_from}/{date_to}", produces = "application/json")

    @Operation(
            summary = "Toll Station Passes",
            description = "Get information for the Passes from a toll station for a period of time"
    )


    public ResponseEntity<?> getTollStationPasses(
            @PathVariable("tollStationID") String tollStationID,
            @PathVariable("date_from") String dateFrom,
            @PathVariable("date_to") String dateTo,
            @RequestParam(required = false, defaultValue = "json") String format) {
        try {
            if (tollStationID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing tollStationID.");
            }
            if (dateFrom == null || dateFrom.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or empty date_from parameter.");
            }
            if (dateTo == null || dateTo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or empty date_to parameter.");
            }

            // Validate and parse dates
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate fromDate = parseDate(dateFrom, dateFormatter);
            LocalDate toDate = parseDate(dateTo, dateFormatter);

            // Validate date range
            if (fromDate.isAfter(toDate)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date range: 'date_from' must be before or equal to 'date_to'.");
            }

            // Fetch data from the service
            Object tollStationPasses = tollStationService.getTollStationPasses(tollStationID, dateFrom, dateTo, format);

            return ResponseEntity.ok(tollStationPasses);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    private LocalDate parseDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for '" + date + "'. Expected format: yyyyMMdd.");
        }
    }
}
