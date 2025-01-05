package com.payway.controllers;

import com.payway.services.TollStationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api")
public class PassesCostController {

    @GetMapping(value = "/passesCost/{tollOpID}/{tagOpID}/{date_from}/{date_to}", produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            summary = "Passes Cost",
            description = "Returns info for the total passes of someone with a certain tag ID from toll stations of someone with other ID"
    )


    public ResponseEntity<?> getTollStationPasses(
            @PathVariable("tollOpID") String tollOpID,
            @PathVariable("tagOpID") String tagOpID,
            @PathVariable("date_from") String dateFrom,
            @PathVariable("date_to") String dateTo,
            @RequestParam(required = false, defaultValue = "json") String format) {
        try {
            if (tollOpID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing tollOpID.");
            }
            if (tagOpID == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing tagOpID.");
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
//            Object passesCost = tollStationService.getTollStationPasses(tollStationID, dateFrom, dateTo, format);

//            return ResponseEntity.ok(passesCost);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    return null;
    }

    private LocalDate parseDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for '" + date + "'. Expected format: yyyyMMdd.");
        }
    }
}
