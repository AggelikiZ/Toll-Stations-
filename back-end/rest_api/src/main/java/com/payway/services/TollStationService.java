package com.payway.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payway.models.TollStation;
import com.payway.repositories.TollStationRepository;
import com.payway.utils.Json2CSV;
import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TollStationService {
    private final TollStationRepository tollStationRepository;
    private final Json2CSV jsonToCsvConverter;

    public TollStationService(TollStationRepository tollStationRepository) {
        this.tollStationRepository = tollStationRepository;
        this.jsonToCsvConverter = new Json2CSV();
    }

    public void resetStations(String resourcePath) throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found in classpath: " + resourcePath);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            tollStationRepository.deleteAll();
            System.out.println("All toll stations deleted.");

            String line;
            List<TollStation> tollStations = new ArrayList<>();
            int lineNumber = 0;

            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    String[] values = line.split(",", -1); // Handle trailing empty strings

                    // Validate the number of columns
                    if (values.length != 14) {
                        throw new IllegalArgumentException("Invalid number of columns at line " + lineNumber);
                    }

                    // Map CSV columns to TollStation entity
                    TollStation tollStation = new TollStation();
                    tollStation.setOpId(values[0].trim());
                    tollStation.setTollId(values[2].trim());
                    tollStation.setName(values[3].trim());
                    tollStation.setType(values[4].trim());
                    tollStation.setLocality(values[5].trim());
                    tollStation.setRoad(values[6].trim());
                    tollStation.setLat(new BigDecimal(values[7].trim()));
                    tollStation.setLng(new BigDecimal(values[8].trim()));
                    tollStation.setPrice1(new BigDecimal(values[10].trim()));
                    tollStation.setPrice2(new BigDecimal(values[11].trim()));
                    tollStation.setPrice3(new BigDecimal(values[12].trim()));
                    tollStation.setPrice4(new BigDecimal(values[13].trim()));

                    tollStations.add(tollStation);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format at line " + lineNumber + ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error processing line " + lineNumber + ": " + e.getMessage());
                }
            }

            tollStationRepository.saveAll(tollStations);
            System.out.println("Toll stations saved successfully: " + tollStations.size() + " entries.");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + resourcePath);
        } catch (IllegalArgumentException e) {
            throw e; // Propagate for controller to handle as 400
        } catch (Exception e) {
            throw new Exception("Failed to reset toll stations: " + e.getMessage(), e); // 500-level error
        }
    }

    public Map<String, Object> getStationDetails(String stationId,LocalDate from,LocalDate to) {
        LocalDateTime timestamp = LocalDateTime.now();

        return tollStationRepository.findTollStationPassesByIdAndTimeRange(stationId,from ,to, timestamp);
    }


    public ResponseEntity<?> getTollStationPasses(String tollStationID, LocalDate date_from, LocalDate date_to, String format) throws Exception {
        try {

            Map<String, Object> response = getStationDetails(tollStationID, date_from, date_to);

            if (response.isEmpty()) {
                return null;
            }
            if ("csv".equalsIgnoreCase(format)) {
                String json = (String) response.get("passDetails");

                // Convert JSON to CSV
                String csv = jsonToCsvConverter.convertJsonToCsv("[" + json + "]");

                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=toll_station_passes.csv")
                        .body(csv);
            }

            return ResponseEntity.ok(response);
        }
        catch(Exception e) {
            throw new Exception("Failed to get toll station passes: " + e.getMessage(), e);
        }
    }
}