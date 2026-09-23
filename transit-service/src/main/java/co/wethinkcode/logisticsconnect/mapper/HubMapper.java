package co.wethinkcode.logisticsconnect.mapper;

import co.wethinkcode.logisticsconnect.model.dto.HubRequest;
import co.wethinkcode.logisticsconnect.model.dto.HubResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HubMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String jsonStringResponse(HubResponse ingestion) throws JsonProcessingException {
        return objectMapper.writeValueAsString(ingestion);
    }

    public static HubRequest jsonStringRequest(String request) throws JsonProcessingException {
        return objectMapper.readValue(request, HubRequest.class);
    }
}