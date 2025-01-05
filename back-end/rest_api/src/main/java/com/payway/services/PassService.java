package com.payway.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.payway.repositories.PassRepository;
import com.payway.repositories.TagRepository;
import com.payway.models.Pass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
                    //pass.setField1(values[0].trim()); // Example field mapping
                    //pass.setField2(values[1].trim());
                    // Continue mapping fields...

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


}
