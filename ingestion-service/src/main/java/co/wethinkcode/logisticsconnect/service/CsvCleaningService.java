package co.wethinkcode.logisticsconnect.service;

import co.wethinkcode.logisticsconnect.model.entity.Ingestion;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CsvCleaningService {

    private static final String CSV_RESOURCE = "hubs-global.csv";

    private static final Set<String> TRUE_VALUES = Set.of("y", "yes", "1", "true");

    private static final Map<String, String> PROVINCES = Map.ofEntries(
            Map.entry("gauteng", "Gauteng"),
            Map.entry("eastern cape", "Eastern Cape"),
            Map.entry("western cape", "Western Cape"),
            Map.entry("northern cape", "Northern Cape"),
            Map.entry("kwazulu-natal", "KwaZulu-Natal"),
            Map.entry("kwazulu natal", "KwaZulu-Natal"),
            Map.entry("kwa-zulu natal", "KwaZulu-Natal"),
            Map.entry("free state", "Free State"),
            Map.entry("north west", "North West"),
            Map.entry("limpopo", "Limpopo"),
            Map.entry("mpumalanga", "Mpumalanga")
    );

    public List<Ingestion> cleanAndDeduplicate() throws IOException, CsvException {
        List<Ingestion> cleaned = new ArrayList<>();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CSV_RESOURCE)) {
            if (input == null) {
                throw new IOException("CSV resource not found: " + CSV_RESOURCE);
            }
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
                 CSVReader csv = new CSVReader(reader)) {
                if (csv.readNext() == null) {
                    throw new IOException("CSV is empty");
                }
                String[] row;
                while ((row = csv.readNext()) != null) {
                    cleaned.add(cleanRow(row));
                }
            }
        }
        return deduplicate(cleaned);
    }

    private Ingestion cleanRow(String[] row) {
        return new Ingestion(
                cleanHubId(cell(row, 0)),
                cleanProvince(cell(row, 1)),
                cleanSortingCenter(cell(row, 2)),
                cleanActive(cell(row, 3))
        );
    }

    private String cleanHubId(String value) {
        return collapseSpaces(value).toUpperCase(Locale.ROOT);
    }

    private String cleanProvince(String value) {
        String key = collapseSpaces(value).toLowerCase(Locale.ROOT);
        return PROVINCES.getOrDefault(key, titleCase(key));
    }

    private String cleanSortingCenter(String value) {
        return titleCase(collapseSpaces(value));
    }

    private boolean cleanActive(String value) {
        return TRUE_VALUES.contains(collapseSpaces(value).toLowerCase(Locale.ROOT));
    }

    private List<Ingestion> deduplicate(List<Ingestion> cleaned) {
        Map<String, List<Ingestion>> byCenter = new LinkedHashMap<>();
        for (Ingestion ingestion : cleaned) {
            byCenter.computeIfAbsent(ingestion.getSortingCenter(), key -> new ArrayList<>()).add(ingestion);
        }

        List<Ingestion> result = new ArrayList<>();
        for (List<Ingestion> group : byCenter.values()) {
            result.add(resolveGroup(group));
        }
        result.sort(Comparator.comparing(Ingestion::getHubId));
        return result;
    }

    private Ingestion resolveGroup(List<Ingestion> group) {
        Ingestion canonical = group.stream()
                .filter(ingestion -> !ingestion.getProvince().isEmpty())
                .findFirst()
                .orElseGet(() -> group.get(0));

        long trueCount = group.stream().filter(Ingestion::isActive).count();
        boolean active;
        if (trueCount * 2 == group.size()) {
            active = canonical.isActive();
        } else {
            active = trueCount > group.size() / 2;
        }

        return new Ingestion(canonical.getHubId(), canonical.getProvince(), canonical.getSortingCenter(), active);
    }

    private String cell(String[] row, int index) {
        return index < row.length ? row[index] : "";
    }

    private String collapseSpaces(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    private String titleCase(String value) {
        if (value.isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (String word : value.split(" ")) {
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(word.isEmpty() ? word : Character.toTitleCase(word.charAt(0)) + word.substring(1));
        }
        return out.toString();
    }
}