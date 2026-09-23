package co.wethinkcode.logisticsconnect.model.dto;

public record HubResponse(
        String hubId,
        String province,
        String sortingCenter,
        int stage,
        boolean isActive
) {

}