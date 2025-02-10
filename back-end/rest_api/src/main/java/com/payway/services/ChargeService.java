package com.payway.services;

import com.payway.models.Debt;
import com.payway.models.Pass;
import com.payway.models.Tag;
import com.payway.models.TollStation;
import com.payway.repositories.*;
import org.springframework.stereotype.Service;
import com.payway.utils.Json2CSV;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



@Service
public class ChargeService {

    private final OperatorRepository operatorRepository;
    private final PassRepository passRepository;
    private final TagRepository tagRepository;
    private final TollStationRepository tollStationRepository;
    private final ObjectMapper objectMapper;

    public ChargeService(OperatorRepository operatorRepository, PassRepository passRepository, TagRepository tagRepository, TollStationRepository tollStationRepository) {
        this.operatorRepository = operatorRepository;
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
        this.tollStationRepository = tollStationRepository;
        this.objectMapper = new ObjectMapper();
    }


    public Object getChargesBy(String tollOpID, String dateFromStr, String dateToStr, String format) throws Exception{
        String requestTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if (!operatorRepository.existsByOpId(tollOpID)) {
            throw new IllegalArgumentException("Invalid tollOpID: " + tollOpID);
        }
        // 1. Έλεγχος αν η ημερομηνία είναι σε σωστό format
        LocalDateTime dateFrom, dateTo;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            dateFrom = LocalDate.parse(dateFromStr, formatter).atStartOfDay();
            dateTo = LocalDate.parse(dateToStr, formatter).atStartOfDay();
            if (dateFrom.isAfter(dateTo)) {
                throw new IllegalArgumentException("dateFrom cannot be after dateTo.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        List<TollStation> stations = tollStationRepository.findByOpId(tollOpID);
        Set<String> stationIds = stations.stream().map(TollStation::getTollId).collect(Collectors.toSet());

        List<Pass> passes = passRepository.findPassesByStationIdsAndDateRangeAndVisitingOperator(stationIds, dateFrom, dateTo, tollOpID);

        // Αν δεν υπάρχουν διελεύσεις (passes), επιστρέφουμε κενή λίστα, ώστε ο Controller να επιστρέψει 204 No Content
        if (passes.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<Pass>> groupedByOperator = passes.stream()
                .collect(Collectors.groupingBy(pass -> {
                    Tag tag = tagRepository.findById(pass.getTagRef()).orElse(null);
                    return tag != null ? tag.getOpId() : "unknown";
                }));

        List<Map<String, Object>> vOpList = new ArrayList<>();

        groupedByOperator.forEach((visitingOpId, passList) -> {
            int nPasses = passList.size();
            BigDecimal passesCost = passList.stream()
                    .map(Pass::getCharge)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> visitingOperatorData = new HashMap<>();
            visitingOperatorData.put("visitingOpID", visitingOpId);
            visitingOperatorData.put("nPasses", nPasses);
            visitingOperatorData.put("passesCost", passesCost);

            vOpList.add(visitingOperatorData);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("tollOpID", tollOpID);
        response.put("requestTimestamp", requestTimestamp);
        response.put("periodFrom", dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.put("periodTo", dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.put("vOpList", vOpList);

        // Αν ζητηθεί CSV format, επιστρέφουμε CSV
        if ("csv".equalsIgnoreCase(format)) {
            try {
                String jsonResponse = objectMapper.writeValueAsString(vOpList);
                Json2CSV json2CSV = new Json2CSV();
                return json2CSV.convertJsonToCsv(jsonResponse);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating CSV");
            }
        }

        return response;
    }



}

