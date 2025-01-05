package com.payway.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class Json2CSV {

    private final ObjectMapper objectMapper;

    public Json2CSV() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Converts JSON string to CSV format.
     *
     * @param json The JSON string to be converted.
     * @return CSV representation of the JSON.
     * @throws Exception If the JSON cannot be parsed or CSV cannot be generated.
     */
    public String convertJsonToCsv(String json) throws Exception {
        // Parse the JSON string into a List of Maps
        List<Map<String, Object>> records = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});

        // Return empty CSV if no records are found
        if (records.isEmpty()) {
            return "";
        }

        // Generate CSV
        StringWriter csvWriter = new StringWriter();
        try (CSVWriter writer = new CSVWriter(csvWriter)) {
            // Write header (keys of the first map)
            String[] headers = records.get(0).keySet().toArray(new String[0]);
            writer.writeNext(headers);

            // Write rows (values of each map)
            for (Map<String, Object> record : records) {
                String[] row = record.values().stream()
                        .map(value -> value == null ? "" : value.toString())
                        .toArray(String[]::new);
                writer.writeNext(row);
            }
        }

        return csvWriter.toString();
    }
}
