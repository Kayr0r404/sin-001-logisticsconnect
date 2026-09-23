package co.wethinkcode.logisticsconnect.mapper;

import co.wethinkcode.logisticsconnect.model.dto.*;
import co.wethinkcode.logisticsconnect.model.entity.Hub;

public class HubMapper {

    public static HubResponse toDto(Hub ingestion) {
        return new HubResponse(
                ingestion.getHubId(),
                ingestion.getProvince(),
                ingestion.getSortingCenter(),
                ingestion.getStage(),
                ingestion.isActive()
        );
    }

    public static Hub fromDto(HubRequest request) {
        return new Hub(
                request.hubId(),
                request.province(),
                request.sortingCenter(),
                request.stage(),
                request.isActive()
        );
    }
}