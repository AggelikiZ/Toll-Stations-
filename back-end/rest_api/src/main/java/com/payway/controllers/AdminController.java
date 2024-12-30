package com.payway.controllers;

import com.payway.models.Generic500Response;
import com.payway.models.ResetStations200Response;
import com.payway.models.ResetStations400Response;
import com.payway.services.TollStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController{

    private final TollStationService tollStationService;

    public AdminController(TollStationService tollStationService) {
        this.tollStationService = tollStationService;
    }

    @PostMapping(value = "/resetstations", produces = "application/json")
    @Operation(
            summary = "Reset toll stations",
            description = "Resets the toll stations table with the data from the `tollstations2024.csv` file."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset successful", content = @Content(schema = @Schema(implementation = ResetStations200Response.class))),
            @ApiResponse(responseCode = "400", description = "Reset failed due to illegal argument", content = @Content(schema = @Schema(implementation = ResetStations400Response.class))),
            @ApiResponse(responseCode = "500", description = "Reset failed", content = @Content(schema = @Schema(implementation = Generic500Response.class)))
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




}
