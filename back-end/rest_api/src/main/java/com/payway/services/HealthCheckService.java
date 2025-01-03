package com.payway.services;

import com.payway.repositories.HealthCheckRepository;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthCheckService {

    private final HealthCheckRepository healthCheckRepository;

    public HealthCheckService(HealthCheckRepository healthCheckRepository) {
        this.healthCheckRepository = healthCheckRepository;
    }

    public Map<String, Object> getHealthStatus()  throws Exception  {
        Map<String, Object> response = new HashMap<>();
        try {
            // Fetch data using HealthCheckRepository
            long nStations = healthCheckRepository.countStations(); // Method to count stations
            long nTags = healthCheckRepository.countTags();         // Method to count tags
            long nPasses = healthCheckRepository.countPasses();     // Method to count passes

            // Populate the healthcheck response
            response.put("status", "OK");
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("dbConnection", "active");
            response.put("nStations", nStations);
            response.put("nTags", nTags);
            response.put("nPasses", nPasses);

        } catch (Exception e) {
            throw new Exception("Failed to establish connection: " + e.getMessage(), e);
        }
        return response;
    }
}
