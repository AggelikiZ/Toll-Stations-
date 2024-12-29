package com.payway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TollStationPassesController {
    @GetMapping(value = "/tollStationPasses/{tollStationID}/{date_from}/{date_to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTollStationPasses(
            @PathVariable String tollStationID,
            @PathVariable String date_from,
            @PathVariable String date_to,
            @RequestParam(required = false, defaultValue = "json") String format) {

        // Validate the date format (basic validation for YYYYMMDD)
        if (!date_from.matches("\\d{8}") || !date_to.matches("\\d{8}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Use YYYYMMDD.");
        }

        // Dummy data for demonstration
        Map<String, Object> response = new HashMap<>();
        response.put("stationID", tollStationID);
        response.put("stationOperator", "No one");
        response.put("requestTimestamp", LocalDateTime.now().toString());
        response.put("periodFrom", date_from);
        response.put("periodTo", date_to);
        response.put("nPasses", 3);

        List<Map<String, Object>> passList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> pass = new HashMap<>();
            pass.put("passIndex", i);
            pass.put("passID", "PASS" + i);
            pass.put("timestamp", "2023-12-25 12:0" + i);
            pass.put("tagID", "TAG" + i);
            pass.put("tagProvider", "Provider" + i);
            pass.put("passType", i % 2 == 0 ? "home" : "visitor");
            pass.put("passCharge", "10.00");
            passList.add(pass);
        }
        response.put("passList", passList);

        // Return response in the requested format
        if ("csv".equalsIgnoreCase(format)) {
            StringBuilder csvResponse = new StringBuilder();
            csvResponse.append("passIndex,passID,timestamp,tagID,tagProvider,passType,passCharge\n");
            for (Map<String, Object> pass : passList) {
                csvResponse.append(String.join(",",
                                pass.get("passIndex").toString(),
                                pass.get("passID").toString(),
                                pass.get("timestamp").toString(),
                                pass.get("tagID").toString(),
                                pass.get("tagProvider").toString(),
                                pass.get("passType").toString(),
                                pass.get("passCharge").toString()))
                        .append("\n");
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(csvResponse.toString());
        }

        return ResponseEntity.ok(response);
    }
}
