package co.wethinkcode.logisticsconnect.model.dto;

import java.util.UUID;

public record IngestionResponse(
        UUID id,
        String hubId,
        String province,
        String sortingCenter,
        boolean isActive
) {

}