package com.payway.controllers;

import com.payway.models.*;
import com.payway.services.PassService;
import com.payway.services.TollStationService;
import com.payway.services.HealthCheckService;
import com.payway.services.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController{

    private final TollStationService tollStationService;
    private final HealthCheckService healthcheckService;
    private final PassService passService;
    private final StationService stationService;

    public AdminController(TollStationService tollStationService, HealthCheckService healthcheckService, PassService passService, StationService stationService) {
        this.tollStationService = tollStationService;
        this.healthcheckService = healthcheckService;
        this.passService = passService;
        this.stationService = stationService;
    }
    @PostMapping(value = "/resetstations", produces = "application/json")
    @Operation(
            summary = "Reset toll stations",
            description = "Resets the toll stations table with the data from the `tollstations2024.csv` file."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset successful", content = @Content(schema = @Schema(implementation = ResetStations200Response.class))),
            @ApiResponse(responseCode = "400", description = "Reset failed due to illegal argument", content = @Content(schema = @Schema(implementation = ResetStations400Response.class))),
            @ApiResponse(responseCode = "500", description = "Reset failed", content = @Content(schema = @Schema(implementation = Generic500Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized401Response.class)))
    })
    public ResponseEntity<?> resetStations() {
        try {
            String resourcePath = "csv/tollstations2024.csv";
            tollStationService.resetStations(resourcePath);
            return ResponseEntity.ok(new ResetStations200Response().status("OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResetStations400Response().status("failed").info(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Generic500Response().status("failed").info(e.getMessage()));
        }
    }

    @PostMapping(value = "/resetpasses", produces = "application/json")
    @Operation(
            summary = "Reset passes",
            description = "Resets the toll stations table with the data from the `tollstations2024.csv` file."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset successful", content = @Content(schema = @Schema(implementation = ResetStations200Response.class))),
            @ApiResponse(responseCode = "400", description = "Reset failed due to illegal argument", content = @Content(schema = @Schema(implementation = ResetStations400Response.class))),
            @ApiResponse(responseCode = "500", description = "Reset failed", content = @Content(schema = @Schema(implementation = Generic500Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized401Response.class)))
    })
    public ResponseEntity<?> resetPasses() {
        try {
            passService.resetPasses();
            return ResponseEntity.ok(new ResetStations200Response().status("OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResetStations400Response().status("failed").info(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Generic500Response().status("failed").info(e.getMessage()));
        }
    }

    @PostMapping(value = "/addpasses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    @Operation(
            summary = "Add passes",
            description = "Adds passes and updates data structures with the data from the provided csv file."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Update successful",content = @Content(schema = @Schema(implementation = ResetStations200Response.class))),
            @ApiResponse(responseCode = "400", description = "Update failed due to illegal argument", content = @Content(schema = @Schema(implementation = ResetStations400Response.class))),
            @ApiResponse(responseCode = "500", description = "Update failed due to interval server error", content = @Content(schema = @Schema(implementation = Generic500Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized401Response.class)))
    })
    public ResponseEntity<?> addPasses(@RequestParam("file") MultipartFile file) {
        try {
            if (!Objects.equals(file.getContentType(), "text/csv")) {
                return ResponseEntity.badRequest().body(Map.of("status", "failed", "info", "Invalid file type. Expected text/csv."));
            }
            passService.addPasses(file);
            return ResponseEntity.ok(Map.of("status", "OK"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "info", e.getMessage()));
        } catch (Exception e) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Generic500Response().status("failed").info(e.getMessage()));
        }
    }

    //Healthcheck Controller
    @GetMapping(value = "/healthcheck", produces = "application/json")
    public ResponseEntity<?> healthCheck() {
        try {
            // Call the service to get health status
            Map<String, Object> healthStatus = healthcheckService.getHealthStatus();
            return ResponseEntity.ok(healthStatus);
        } catch (Exception e) {
            // Handle failure and return 401 with error details
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "failed");
            errorResponse.put("dbconnection", e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);

        }
    }

    @GetMapping("/stations")
    public ResponseEntity<List<TollStation>> getAllStations() {
        List<TollStation> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

}


