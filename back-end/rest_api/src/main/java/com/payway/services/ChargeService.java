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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChargeService {

    private final DebtRepository debtRepository;
    private final PassRepository passRepository;
    private final TagRepository tagRepository;
    private final TollStationRepository tollStationRepository;

    public ChargeService(DebtRepository debtRepository, PassRepository passRepository, TagRepository tagRepository, TollStationRepository tollStationRepository) {
        this.debtRepository = debtRepository;
        this.passRepository = passRepository;
        this.tagRepository = tagRepository;
        this.tollStationRepository = tollStationRepository;
    }


    public Map<String, Object> getChargesBy(String tollOpID, LocalDateTime dateFrom, LocalDateTime dateTo) {
        // Format timestamp
        String requestTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // 🔹 Έλεγχος αν υπάρχει το tollOpID πριν γίνει οποιαδήποτε αναζήτηση
        boolean operatorExists = tollStationRepository.existsByOpId(tollOpID);
        if (!operatorExists) {
            throw new IllegalArgumentException("Invalid tollOpID: " + tollOpID);
        }

        // Fetch toll stations belonging to the operator
        List<TollStation> stations = tollStationRepository.findByOpId(tollOpID);
        Set<String> stationIds = stations.stream().map(TollStation::getTollId).collect(Collectors.toSet());

        // Fetch passes directly with visiting operators
        List<Pass> passes = passRepository.findPassesByStationIdsAndDateRangeAndVisitingOperator(stationIds, dateFrom, dateTo, tollOpID);

        // Group by visiting operator
        Map<String, List<Pass>> groupedByOperator = passes.stream()
                .collect(Collectors.groupingBy(pass -> {
                    Tag tag = tagRepository.findById(pass.getTagRef()).orElse(null);
                    return tag != null ? tag.getOpId() : "unknown";
                }));

        // Build vOpList
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

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("tollOpID", tollOpID);
        response.put("requestTimestamp", requestTimestamp);
        response.put("periodFrom", dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.put("periodTo", dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        response.put("vOpList", vOpList);

        return response;
    }

}

