package com.payway.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payway.models.*;
import com.payway.models.LoginRequest;
import com.payway.models.Operator;
import com.payway.models.TollStation;
import com.payway.models.User;
import com.payway.repositories.UserRepository;
import com.payway.services.PassService;
import com.payway.utils.Json2CSV;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Passes Analysis",
            description = "Get information for the passes of a vehicle with tag from an operator from stations of an operator for a period of time."
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

    public ResponseEntity<?> getPassAnalysis(
            @PathVariable String stationOpID,
            @PathVariable String tagOpID,
            @PathVariable String date_from,
            @PathVariable String date_to,
            @RequestParam(required = false, defaultValue = "json") String format
    ) {
        if (stationOpID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing stationOpID.");
        }
        if (tagOpID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing tagOpID.");
        }
        if (date_from == null || date_from.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or empty date_from parameter.");
        }
        if (date_to == null || date_to.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or empty date_to parameter.");
        }
        try {
            // Format dates from "YYYYMMDD"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime startDate = LocalDate.parse(date_from, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(date_to, formatter).atTime(23, 59, 59);

            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("startDate cannot be after endDate.");
            }

            // Call service
            Object response = passService.getPassAnalysis(stationOpID, tagOpID, startDate, endDate, format);

            // Προσθήκη ελέγχου για `204 No Content`
            if (response == null || (response instanceof List && ((List<?>) response).isEmpty())) {
                return ResponseEntity.noContent().build();
            }

            // Αν ζητηθεί CSV, επιστρέφουμε CSV αρχείο
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
