package com.payway.services;

import com.payway.models.Debt;
import com.payway.models.Pass;
import com.payway.models.Tag;
import com.payway.models.TollStation;
import com.payway.repositories.DebtRepository;
import com.payway.repositories.PassRepository;
import com.payway.repositories.TagRepository;
import com.payway.repositories.TollStationRepository;
import org.springframework.stereotype.Service;
import com.payway.utils.Json2CSV;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class ChargeService {

    private final DebtRepository debtRepository;
    private final PassRepository passRepository;
    private final TagRepository tagRepository;
    private final TollStationRepository tollStationRepository;
    private final ObjectMapper objectMapper;

    public ChargeService(DebtRepository debtRepository, PassRepository passRepository, TagRepository tagRepository, TollStationRepository tollStationRepository) {
        this.debtRepository = debtRepository;
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
        this.tollStationRepository = tollStationRepository;
        this.objectMapper = new ObjectMapper();
    }


    public Object getChargesBy(String tollOpID, LocalDateTime dateFrom, LocalDateTime dateTo, String format) throws Exception {
        String requestTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        boolean operatorExists = tollStationRepository.existsByOpId(tollOpID);
        if (!operatorExists) {
            throw new IllegalArgumentException("Invalid tollOpID: " + tollOpID);
        }

        List<TollStation> stations = tollStationRepository.findByOpId(tollOpID);
        Set<String> stationIds = stations.stream().map(TollStation::getTollId).collect(Collectors.toSet());

        List<Pass> passes = passRepository.findPassesByStationIdsAndDateRangeAndVisitingOperator(stationIds, dateFrom, dateTo, tollOpID);

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

        if ("csv".equalsIgnoreCase(format)) {
            String jsonResponse = objectMapper.writeValueAsString(vOpList);
            Json2CSV json2CSV = new Json2CSV();
            return json2CSV.convertJsonToCsv(jsonResponse); // Επιστρέφουμε CSV ως String
        }

        return response;
    }

}

