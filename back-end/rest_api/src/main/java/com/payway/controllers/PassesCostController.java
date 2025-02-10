package com.payway.controllers;

import com.opencsv.CSVWriter;
import com.payway.models.Generic500Response;
import com.payway.models.Unauthorized401Response;
import com.payway.models.passesCostDetails;
import com.payway.repositories.TollStationRepository;
import com.payway.services.TollStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PassesCostController {

    private final PassService passService;
    private final TollStationRepository tollStationRepository;

    @Autowired
    public PassesCostController(PassService passService, TollStationRepository tollStationRepository) {
        this.passService = passService;
        this.tollStationRepository = tollStationRepository;
    }

    @GetMapping(value = "/passesCost/{tollOpID}/{tagOpID}/{date_from}/{date_to}", produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(
            summary = "Passes Cost",
            description = "Get information on the number of passes with a tag of an operator from stations of an operator and total cost for a period of time"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful Response",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(value = "{\"Requested Data Names\": \"Requested Data Values\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(value = "{\"error\": \"Bad request: <Error Analysis>\"}")
                    )),
            @ApiResponse(responseCode = "204", description = "Successful but no content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials",
                    content = @Content(schema = @Schema(implementation = Unauthorized401Response.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Generic500Response.class)))
    })


    public ResponseEntity<?> getpassesCost(
            @PathVariable("tollOpID") String tollOpID,
            @PathVariable("tagOpID") String tagOpID,
            @PathVariable("date_from") String dateFrom,
            @PathVariable("date_to") String dateTo,
            @RequestParam(required = false, defaultValue = "json") String format) {
        try {
            if (tollOpID == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: Missing tollOpID.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (tagOpID == null) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: Missing tagOpID.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (dateFrom == null || dateFrom.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: Missing or empty date_from parameter.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (dateTo == null || dateTo.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: Missing or empty date_from parameter.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (tollStationRepository.tollOpExists(tollOpID) == 0){
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: Invalid tollOpID.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (tollStationRepository.tollOpExists(tagOpID) == 0){
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: Invalid tagOpID.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validate and parse dates
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate fromDate = parseDate(dateFrom, dateFormatter);
            LocalDate toDate = parseDate(dateTo, dateFormatter);

            // Validate date range
            if (fromDate.isAfter(toDate)) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Bad request: 'date_from' must be before or equal to 'date_to'.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
            Map<String, String> response = new HashMap<>();
            response.put("error", "Bad request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private LocalDate parseDate(String date, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "'date_from' must be before or equal to 'date_to'.");
            throw new IllegalArgumentException("Invalid date format for '" + date + "'. Expected format: yyyyMMdd.");
        }
    }

    public String convertTotalCostDetailsToCSV(passesCostDetails costDetails) {
        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Write the cost details header
            csvWriter.writeNext(new String[]{
                    "TollOpID", "TagOpID", "RequestTimestamp", "PeriodFrom", "PeriodTo", "nPasses", "passesCost"
            });
            csvWriter.writeNext(new String[]{
                    costDetails.gettollOpID(),
                    costDetails.gettagOpID(),
                    costDetails.getRequestTimestamp().toString(),
                    costDetails.getPeriodFrom(),
                    costDetails.getPeriodTo(),
                    String.valueOf(costDetails.getnPasses()),
                    String.valueOf(costDetails.getPassesCost())
            });

        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV: " + e.getMessage(), e);
        }
        return writer.toString();
    }

}
