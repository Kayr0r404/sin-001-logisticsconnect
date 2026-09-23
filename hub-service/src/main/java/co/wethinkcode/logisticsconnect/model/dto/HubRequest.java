package co.wethinkcode.logisticsconnect.model.dto;

public record HubRequest(
        String hubId,
        String province,
        String sortingCenter,
        int stage,
        boolean isActive
) {

}