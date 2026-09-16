package co.wethinkcode.logisticsconnect.model.dto;

public record IngestionRequest(
        String hubId,
        String province,
        String sortingCenter,
        boolean isActive
) {

}