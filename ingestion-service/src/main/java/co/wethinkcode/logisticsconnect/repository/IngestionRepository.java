package co.wethinkcode.logisticsconnect.repository;

import co.wethinkcode.logisticsconnect.model.dto.IngestionRequest;
import co.wethinkcode.logisticsconnect.model.dto.IngestionResponse;
import co.wethinkcode.logisticsconnect.model.entity.Ingestion;

import java.sql.PreparedStatement;
import java.util.List;

public class IngestionRepository {

    public void save(Ingestion ingestion) {
        String query = """
                INSERT INTO ingestion(hub_id, province, sorting_center, is_active)
                VALUES(?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.PreparedStatement(query)) {
            statement.setString(1, ingestion.getHubId());
            statement.setString(2, ingestion.getProvince());
            statement.setString(3, ingestion.getSortingCenter());
            statement.setBoolean(4, ingestion.isActive());

            statement.executeUpdate();
        }

    }

    public IngestionResponse getByHubId(String hubId) {
        return null;
    }

    public List<IngestionResponse> getByProvince(String province) {
        return null;
    }

    public List<IngestionResponse> getBySortingCenter(String sortingCenter) {
        return null;
    }

    public List<IngestionResponse> getAllActive() {
        return null;
    }

    public List<IngestionResponse> getAll() {
        return null;
    }

}