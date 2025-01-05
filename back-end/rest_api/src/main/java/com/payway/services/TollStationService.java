package com.payway.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payway.models.TollStation;
import com.payway.repositories.TollStationRepository;
import org.apache.catalina.connector.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TollStationService {
    private final TollStationRepository tollStationRepository;

    public TollStationService(TollStationRepository tollStationRepository) {
        this.tollStationRepository = tollStationRepository;
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

    public List<Map<String, Object>> getStationDetails(String stationId) {
        return tollStationRepository.findTollStationPassesById(stationId);
    }

    public ObjectMapper objectMapper;

    public ResponseEntity<?> getTollStationPasses(String tollStationID, String date_from, String date_to, String format) throws Exception {
        try {


            List<Map<String, Object>> response = getStationDetails(tollStationID);


//            String jsonPassDetails = "[" + response + "]"; // Wrap in brackets to make it a valid JSON array

            // Convert the result into JSON using ObjectMapper
//            return objectMapper.writeValueAsString(response);


            // Return response in csv, or else in json
//            if ("csv".equalsIgnoreCase(format)) {
//                StringBuilder csvResponse = new StringBuilder();
//                csvResponse.append("passIndex,passID,timestamp,tagID,tagProvider,passType,passCharge\n");
//                for (Map<String, Object> pass : passList) {
//                    csvResponse.append(String.join(",",
//                                    pass.get("passIndex").toString(),
//                                    pass.get("passID").toString(),
//                                    pass.get("timestamp").toString(),
//                                    pass.get("tagID").toString(),
//                                    pass.get("tagProvider").toString(),
//                                    pass.get("passType").toString(),
//                                    pass.get("passCharge").toString()))
//                            .append("\n");
//                }
//                return ResponseEntity.ok()
//                        .contentType(MediaType.TEXT_PLAIN)
//                        .body(csvResponse.toString());
//            }

            return ResponseEntity.ok(response);
        }
        catch(Exception e) {
            throw new Exception("Failed to get toll station passes: " + e.getMessage(), e);
        }
    }
}