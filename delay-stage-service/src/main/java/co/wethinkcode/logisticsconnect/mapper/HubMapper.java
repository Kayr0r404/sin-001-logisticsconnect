package co.wethinkcode.logisticsconnect.mapper;

import co.wethinkcode.logisticsconnect.model.dto.*;
import co.wethinkcode.logisticsconnect.model.entity.Hub;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class HubMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toDto(Hub ingestion) throws JsonProcessingException {
        HubResponse responseModel = new HubResponse(
                ingestion.getHubId(),
                ingestion.getStage()
        );
        return objectMapper.writeValueAsString(responseModel);
    }

    public static Hub fromDto(String jsonStringRequest) throws JsonProcessingException {
        HubRequest request = objectMapper.readValue(jsonStringRequest, HubRequest.class);

        return new Hub(
                request.hubId(),
                request.province(),
                request.sortingCenter(),
                request.stage(),
                request.isActive()
        );
    }
}