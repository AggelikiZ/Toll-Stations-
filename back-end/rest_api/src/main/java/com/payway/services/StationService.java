package com.payway.services;

import com.payway.models.TollStation;
import com.payway.repositories.StationRepository;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<TollStation> getAllStations() {
        return stationRepository.findAll();
    }
}
