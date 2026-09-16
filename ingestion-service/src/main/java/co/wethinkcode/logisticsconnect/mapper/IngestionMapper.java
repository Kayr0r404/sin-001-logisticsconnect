package co.wethinkcode.logisticsconnect.mapper;

import co.wethinkcode.logisticsconnect.model.dto.IngestionRequest;
import co.wethinkcode.logisticsconnect.model.dto.IngestionResponse;
import co.wethinkcode.logisticsconnect.model.entity.Ingestion;

public class IngestionMapper {

    public static IngestionResponse toDto(Ingestion ingestion) {
        return new IngestionResponse(
                ingestion.getId(),
                ingestion.getHubId(),
                ingestion.getProvince(),
                ingestion.getSortingCenter(),
                ingestion.isActive()
        );
    }

    public static Ingestion fromDto(IngestionRequest request) {
        return new Ingestion(
                null,
                request.hubId(),
                request.province(),
                request.sortingCenter(),
                request.isActive()
        );
    }
}