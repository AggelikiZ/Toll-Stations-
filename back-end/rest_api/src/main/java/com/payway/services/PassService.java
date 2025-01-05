package com.payway.services;

import org.springframework.stereotype.Service;
import com.payway.repositories.PassRepository;
import com.payway.repositories.TagRepository;
import com.payway.models.Pass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@Service
public class PassService {

    private final PassRepository passRepository;
    private final TagRepository tagRepository;

    public PassService(PassRepository passRepository, TagRepository tagRepository) {
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
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

            // Skip the header
            reader.readLine();

            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    // Split CSV line
                    String[] values = line.split(",", -1); // -1 includes trailing empty strings

                    // Validate number of columns (adjust based on the CSV structure)
                    if (values.length != 5) {
                        throw new IllegalArgumentException("Invalid number of columns at line " + lineNumber);
                    }

                    // Map CSV fields to Pass entity
                    Pass pass = new Pass();
                    pass.setStationId(values[0].trim());
                    pass.setTagRef(values[1].trim());
                    pass.setPassTime(LocalDateTime.parse(values[2].trim())); // Adjust date parsing as needed
                    pass.setCharge(new BigDecimal(values[3].trim()));

                    passes.add(pass);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error processing line " + lineNumber + ": " + e.getMessage(), e);
                }
            }

            // Save all passes
            passRepository.saveAll(passes);

        } catch (Exception e) {
            throw new Exception("Failed to process file: " + e.getMessage(), e);
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
}

