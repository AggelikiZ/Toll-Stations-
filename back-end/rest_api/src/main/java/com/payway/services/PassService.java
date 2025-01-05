package com.payway.services;
import com.payway.models.Tag;
import com.payway.repositories.TollStationRepository;
import com.payway.utils.Json2CSV;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.payway.repositories.PassRepository;
import com.payway.repositories.TagRepository;
import com.payway.models.Pass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@Service
public class PassService {
    private final PassRepository passRepository;
    private final TagRepository tagRepository;
    private final TollStationRepository tollStationRepository;
    private final Json2CSV jsonToCsvConverter;

    public PassService(PassRepository passRepository, TagRepository tagRepository, TollStationRepository tollStationRepository) {
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
        this.tollStationRepository = tollStationRepository;
        this.jsonToCsvConverter = new Json2CSV();
    }
    public void resetPasses() {
        try {
            // Διαγραφή των διελεύσεων (passes)
            passRepository.deleteAll();

            // Διαγραφή των εξαρτώμενων δεδομένων (πχ: tags, αν υπάρχουν)
            tagRepository.deleteAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to reset passes and dependent data: " + e.getMessage());
        }
    }

    public void addPasses(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Pass> passes = new ArrayList<>();
            List<Tag> newTags = new ArrayList<>();
            Set<String> existingTags = new HashSet<>(tagRepository.findAllTagRefs());

            // Skip header
            reader.readLine();

            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    // Split CSV line
                    String[] values = line.split(",", -1); // Handle trailing empty strings

                    // Validate number of columns
                    if (values.length != 5) {
                        throw new IllegalArgumentException("Invalid number of columns at line " + lineNumber);
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // Map CSV fields to Pass
                    Pass pass = new Pass();
                    String timestampStr = values[0].trim();
                    if (timestampStr.length() == 16) {
                        timestampStr += ":00"; // Add seconds if missing
                    }
                    pass.setPassTime(Timestamp.valueOf(LocalDateTime.parse(timestampStr, formatter)).toLocalDateTime());
                    String station_id = values[1].trim();
                    if (!tollStationRepository.existsById(station_id)) {
                        String mess = "Invalid toll station ID: " + station_id;
                        throw new IllegalArgumentException(mess);
                    }
                    pass.setStationId(station_id);
                    pass.setTagRef(values[2].trim());
                    pass.setCharge(new BigDecimal(values[4].trim()));
                    passes.add(pass);

                    // Update Tag if it's a new tag reference
                    String tagRef = values[2].trim();
                    if (!existingTags.contains(tagRef)) {
                        Tag tag = new Tag();
                        tag.setTagRef(tagRef);
                        tag.setOpId(values[3].trim());
                        newTags.add(tag);
                        existingTags.add(tagRef); // Avoid duplicate processing
                    }

                } catch (Exception e) {
                    throw new IllegalArgumentException("Error processing line " + lineNumber + ": " + e.getMessage(), e);
                }
            }

            // Save new tags and all passes
            tagRepository.saveAll(newTags);
            passRepository.saveAll(passes);
            System.out.println("Passes saved successfully: " + passes.size() + " entries.");
            System.out.println("Tags saved successfully: " + newTags.size() + " entries.");
        } catch (Exception e) {
            throw new Exception("Failed to process passes: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getPassAnalysis(String stationID, String tagID, LocalDateTime dateFrom, LocalDateTime dateTo) {
        // Ανακτούμε τις διελεύσεις από τη βάση δεδομένων
        List<Pass> passes = passRepository.findPassesByStationAndTagAndDateRange(stationID, tagID, dateFrom, dateTo);

        // Μετατρέπουμε τις διελεύσεις στη σωστή μορφή
        List<Map<String, Object>> passList = new ArrayList<>();
        int index = 1;

        for (Pass pass : passes) {
            passList.add(Map.of(
                    "passIndex", index++,
                    "passID", pass.getPassId(),
                    "stationID", pass.getStationId(),
                    "timestamp", pass.getPassTime().toString(),
                    "tagID", pass.getTagRef(),
                    "passCharge", pass.getCharge()
            ));
        }

        // Δημιουργούμε το συνολικό response
        return Map.of(
                "stationOpID", stationID,
                "tagOpID", tagID,
                "requestTimestamp", LocalDateTime.now().toString(),
                "periodFrom", dateFrom.toString(),
                "periodTo", dateTo.toString(),
                "nPasses", passList.size(),
                "passList", passList
        );
    }

    public ResponseEntity<?> getpassesCost(String tollOpID, String tagOpID, LocalDate fromDate,LocalDate toDate,String format) throws Exception{
        try {

//            Map<String, Object> response = PassRepository.getPassesCost(tollOpID, tagOpID, fromDate, toDate);
            Map<String, Object> response = null;
            if (response.isEmpty()) {
                return null;
            }
            if ("csv".equalsIgnoreCase(format)) {
                String json = (String) response.get("passDetails");

                // Convert JSON to CSV
                String csv = jsonToCsvConverter.convertJsonToCsv("[" + json + "]");

                return ResponseEntity.ok()
                        .body(csv);
            }

            return ResponseEntity.ok(response);
        }
        catch(Exception e) {
            throw new Exception("Failed to get toll station passes: " + e.getMessage(), e);
        }
    }
}

