package co.wethinkcode.logisticsconnect.model.dto;

public record IngestionResponse(
        String hubId,
        String province,
        String sortingCenter,
        boolean isActive
) {

}