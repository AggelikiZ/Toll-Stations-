package com.payway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class HealthcheckController {

    @GetMapping(value = "/healthcheck", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> healthCheck() {
        // Create a response map to hold healthcheck information
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("dbConnection", "active");  // Dummy value for demonstration
        response.put("nStations", 5);            // Replace with actual station count
        response.put("nTags", 100);              // Replace with actual tag count
        response.put("nPasses", 2000);           // Replace with actual pass count

        // Return the response as a JSON object
        return ResponseEntity.ok(response);
    }
}