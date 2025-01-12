package com.payway.controllers;

import com.opencsv.CSVWriter;
import com.payway.models.passesCostDetails;
import com.payway.services.TollStationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.payway.services.PassService;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PassesCostController {

    private final PassService passService;

    @Autowired
    public PassesCostController(PassService passService) {
        this.passService = passService;
    }

    @GetMapping(value = "/passesCost/{tollOpID}/{tagOpID}/{date_from}/{date_to}", produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            summary = "Passes Cost",
            description = "Returns info for the total passes of someone with a certain tag ID from toll stations of someone with other ID"
    )


    public ResponseEntity<?> getpassesCost(
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
            passesCostDetails passesCost = passService.totalpassesCost(tollOpID, tagOpID, fromDate, toDate, format);
            // Object passesCost = null;

            if (passesCost == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            if ("csv".equalsIgnoreCase(format)) {
                String csv = convertTotalCostDetailsToCSV(passesCost);
                return ResponseEntity.ok()
                        .header("Content-Disposition", "inline; filename=pass_analysis.csv")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(csv);
            }

            return ResponseEntity.ok(passesCost);

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

    public String convertTotalCostDetailsToCSV(passesCostDetails costDetails) {
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Write the cost details header
            csvWriter.writeNext(new String[]{
                    "TollOpID", "TagOpID", "RequestTimestamp", "PeriodFrom", "PeriodTo", "nPasses", "TotalCost"
            });
            csvWriter.writeNext(new String[]{
                    costDetails.gettollOpID(),
                    costDetails.gettagOpID(),
                    costDetails.getRequestTimestamp().toString(),
                    costDetails.getPeriodFrom(),
                    costDetails.getPeriodTo(),
                    String.valueOf(costDetails.getnPasses()),
                    String.valueOf(costDetails.getTotalCost())
            });

        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV: " + e.getMessage(), e);
        }
        return writer.toString();
    }

}
