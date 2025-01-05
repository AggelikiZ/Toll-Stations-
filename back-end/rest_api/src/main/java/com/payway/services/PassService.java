package com.payway.services;

import com.payway.models.Pass;
import com.payway.repositories.PassRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class PassService {
    private final PassRepository passRepository;
    private final TagRepository tagRepository;

    public PassService(PassRepository passRepository, TagRepository tagRepository) {
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
    }
    public void resetPasses() {}


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

            // Perform additional updates for dependent tables/collections (e.g., tags)
            updateDependentData(passes);
        } catch (Exception e) {
            throw new Exception("Failed to process file: " + e.getMessage(), e);
        }
    }

    private void updateDependentData(List<Pass> passes) {
        // Implement logic to update dependent tables/collections (e.g., tags)
    }

}
