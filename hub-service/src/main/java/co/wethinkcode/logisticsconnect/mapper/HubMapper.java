package co.wethinkcode.logisticsconnect.mapper;

import co.wethinkcode.logisticsconnect.model.dto.HubRequest;
import co.wethinkcode.logisticsconnect.model.dto.HubResponse;
import co.wethinkcode.logisticsconnect.model.entity.Hub;

public class HubMapper {

    public static HubResponse toDto(Hub ingestion) {
        return new HubResponse(
                ingestion.getHubId(),
                ingestion.getProvince(),
                ingestion.getSortingCenter(),
                ingestion.isActive()
        );
    }

    public static Hub fromDto(HubRequest request) {
        return new Hub(
                request.hubId(),
                request.province(),
                request.sortingCenter(),
                request.isActive()
        );
    }
}