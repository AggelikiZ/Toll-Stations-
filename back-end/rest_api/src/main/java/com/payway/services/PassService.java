package com.payway.services;

import com.payway.models.*;
import com.payway.repositories.*;
import com.payway.utils.Json2CSV;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.payway.repositories.PassRepository;
import com.payway.repositories.TagRepository;
import com.payway.repositories.TollStationRepository;
import org.springframework.stereotype.Service;
import com.payway.models.passesCostDetails;
import com.payway.utils.Json2CSV;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.DecimalFormat;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PassService {

    private final PassRepository passRepository;
    private final TagRepository tagRepository;
    private final TollStationRepository tollStationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final OperatorRepository operatorRepository;

    public PassService(PassRepository passRepository, TagRepository tagRepository, TollStationRepository tollStationRepository, OperatorRepository operatorRepository) {
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
        this.tollStationRepository = tollStationRepository;
        this.operatorRepository = operatorRepository;
    }

    public void resetPasses() {
        try {
            passRepository.deleteAll();
            tagRepository.deleteAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to reset passes and dependent data: " + e.getMessage());
        }
    }

    @Transactional
    public void addPasses(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
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
                    String[] values = line.split(",", -1);

                    if (values.length != 5) {
                        throw new IllegalArgumentException("Invalid number of columns at line " + lineNumber);
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    Pass pass = new Pass();
                    String timestampStr = values[0].trim();
                    if (timestampStr.length() == 16) {
                        timestampStr += ":00";
                    }
                    pass.setPassTime(Timestamp.valueOf(LocalDateTime.parse(timestampStr, formatter)).toLocalDateTime());

                    String stationId = values[1].trim();
                    if (!tollStationRepository.existsById(stationId)) {
                        throw new IllegalArgumentException("Invalid toll station ID: " + stationId);
                    }
                    pass.setStationId(stationId);
                    pass.setTagRef(values[2].trim());
                    pass.setCharge(new BigDecimal(values[4].trim()));
                    if (!passRepository.existsByTagRefAndPassTime(pass.getTagRef(), pass.getPassTime())) {
                        passes.add(pass);
                    }

                    String tagRef = values[2].trim();
                    if (!existingTags.contains(tagRef)) {
                        Tag tag = new Tag();
                        tag.setTagRef(tagRef);
                        tag.setOpId(values[3].trim());
                        newTags.add(tag);
                        existingTags.add(tagRef);
                    }


                } catch (Exception e) {
                    throw new IllegalArgumentException("Error processing line " + lineNumber + ": " + e.getMessage(), e);
                }
            }

            tagRepository.saveAll(newTags);
            passRepository.saveAll(passes);
            System.out.println("Passes saved successfully: " + passes.size() + " entries.");
            System.out.println("Tags saved successfully: " + newTags.size() + " entries.");
        } catch (Exception e) {
            throw new Exception("Failed to process passes: " + e.getMessage(), e);
        }
    }



    public Object getPassAnalysis(String operatorOpID, String tagOpID, LocalDateTime dateFrom, LocalDateTime dateTo, String format) throws Exception {
        boolean operatorExists = operatorRepository.existsByOpId(operatorOpID);
        boolean tagOperatorExists = operatorRepository.existsByOpId(tagOpID);

        if (!operatorExists || !tagOperatorExists) {
            throw new IllegalArgumentException("Invalid stationOpID or tagOpID: " + operatorOpID + ", " + tagOpID);
        }

        List<TollStation> tollStations = tollStationRepository.findByOpId(operatorOpID);
        List<Tag> tags = tagRepository.findByOpId(tagOpID);

        List<Map<String, Object>> passList = new ArrayList<>();
        int index = 1;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (TollStation tollStation : tollStations) {
            for (Tag tag : tags) {
                List<Pass> passes = passRepository.findPassesByStationAndTagAndDateRange(
                        tollStation.getTollId(), tag.getTagRef(), dateFrom, dateTo);

                for (Pass pass : passes) {
                    passList.add(Map.of(
                            "passIndex", index++,
                            "passID", pass.getPassId(),
                            "stationID", pass.getStationId(),
                            "timestamp", pass.getPassTime().format(outputFormatter),
                            "tagID", pass.getTagRef(),
                            "passCharge", pass.getCharge()
                    ));
                }
            }
        }

        // Αν δεν υπάρχουν διελεύσεις (passes), επιστρέφουμε `Collections.emptyList()` για να μπορεί ο Controller να επιστρέψει `204 No Content`
        if (passList.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Object> response = Map.of(
                "stationOpID", operatorOpID,
                "tagOpID", tagOpID,
                "requestTimestamp", LocalDateTime.now().format(outputFormatter),
                "periodFrom", dateFrom.format(outputFormatter),
                "periodTo", dateTo.format(outputFormatter),
                "nPasses", passList.size(),
                "passList", passList
        );

        // Αν ζητηθεί CSV format, ελέγχουμε αν υπάρχουν δεδομένα
        if ("csv".equalsIgnoreCase(format)) {
            if (passList.isEmpty()) {
                return ""; // Επιστροφή κενού CSV αν δεν υπάρχουν δεδομένα
            }
            String jsonResponse = objectMapper.writeValueAsString(passList);
            Json2CSV json2CSV = new Json2CSV();
            return json2CSV.convertJsonToCsv(jsonResponse);
        }

        return response;
    }



    public passesCostDetails totalpassesCost(String tollOpID, String tagOpID, LocalDate date_from, LocalDate date_to, String format) throws Exception {

        try {

            Map<String, Object> response = passRepository.getpassesCost(tollOpID, tagOpID, date_from, date_to);

            if (response.isEmpty()) {
                return null;
            }

            // Create the main DTO object
            passesCostDetails detailsDTO = new passesCostDetails();

            detailsDTO.settollOpID((String) response.get("tollOpID"));
            detailsDTO.settagOpID((String) response.get("tagOpID"));
            detailsDTO.setRequestTimestamp((Timestamp) response.get("requestTimestamp"));
            detailsDTO.setPeriodFrom((String) response.get("periodFrom"));
            detailsDTO.setPeriodTo((String) response.get("periodTo"));
            detailsDTO.setnPasses((Long) response.get("nPasses"));
//            if (detailsDTO.gettagOpID().equals(detailsDTO.gettollOpID())){
//                return detailsDTO;
//            }
            detailsDTO.setPassesCost((BigDecimal) response.get("passesCost"));
            return detailsDTO;

        }
        catch(Exception e) {
            throw new Exception("Failed to get toll station passes: " + e.getMessage(), e);
        }
    }

    public List<Operator> getAllOperators() {
        return operatorRepository.findAll();
    }
}
