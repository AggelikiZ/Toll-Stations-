package com.payway.services;
import com.payway.models.Tag;
import com.payway.repositories.TollStationRepository;
import org.springframework.stereotype.Service;
import com.payway.repositories.PassRepository;
import com.payway.repositories.TagRepository;
import com.payway.models.Pass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

@Service
public class PassService {
    private final PassRepository passRepository;
    private final TagRepository tagRepository;
    private final TollStationRepository tollStationRepository;

    public PassService(PassRepository passRepository, TagRepository tagRepository, TollStationRepository tollStationRepository) {
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
        this.tollStationRepository = tollStationRepository;
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

}
