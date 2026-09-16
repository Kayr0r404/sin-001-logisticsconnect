package co.wethinkcode.logisticsconnect.repository;

import co.wethinkcode.logisticsconnect.db.DatabaseConfig;
import co.wethinkcode.logisticsconnect.model.dto.IngestionResponse;
import co.wethinkcode.logisticsconnect.model.entity.Ingestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngestionRepository {

    private final Connection connection;

    public IngestionRepository() {
        try {
            this.connection = DatabaseConfig.getConnection();
            createTable();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to database", e);
        }
    }

    private void createTable() throws SQLException {
        String ddl = """
                CREATE TABLE IF NOT EXISTS ingestion (
                    hub_id         TEXT PRIMARY KEY,
                    province       TEXT,
                    sorting_center TEXT,
                    is_active      BOOLEAN
                )
                """;
        try (PreparedStatement statement = connection.prepareStatement(ddl)) {
            statement.executeUpdate();
        }
    }

    public void save(Ingestion ingestion) {
        String query = """
                INSERT OR REPLACE INTO ingestion(hub_id, province, sorting_center, is_active)
                VALUES(?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ingestion.getHubId());
            statement.setString(2, ingestion.getProvince());
            statement.setString(3, ingestion.getSortingCenter());
            statement.setBoolean(4, ingestion.isActive());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save ingestion record", e);
        }
    }

    public IngestionResponse getByHubId(String hubId) {
        String query = """
                SELECT hub_id, province, sorting_center, is_active
                FROM ingestion
                WHERE hub_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, hubId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch ingestion record by hub id", e);
        }
    }

    public List<IngestionResponse> getByProvince(String province) {
        String query = """
                SELECT hub_id, province, sorting_center, is_active
                FROM ingestion
                WHERE LOWER(province) = LOWER(?)
                """;
        return queryAll(query, province);
    }

    public List<IngestionResponse> getBySortingCenter(String sortingCenter) {
        String query = """
                SELECT hub_id, province, sorting_center, is_active
                FROM ingestion
                WHERE LOWER(sorting_center) = LOWER(?)
                """;
        return queryAll(query, sortingCenter);
    }

    public List<IngestionResponse> getAllActive() {
        String query = """
                SELECT hub_id, province, sorting_center, is_active
                FROM ingestion
                WHERE is_active = true
                """;
        return queryAll(query);
    }

    public List<IngestionResponse> getAll() {
        String query = """
                SELECT hub_id, province, sorting_center, is_active
                FROM ingestion
                """;
        return queryAll(query);
    }

    private List<IngestionResponse> queryAll(String query, String... params) {
        List<IngestionResponse> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapRow(resultSet));
                }
            }
            return results;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query ingestion records", e);
        }
    }

    private IngestionResponse mapRow(ResultSet resultSet) throws SQLException {
        return new IngestionResponse(
                null,
                resultSet.getString("hub_id"),
                resultSet.getString("province"),
                resultSet.getString("sorting_center"),
                resultSet.getBoolean("is_active")
        );
    }

}