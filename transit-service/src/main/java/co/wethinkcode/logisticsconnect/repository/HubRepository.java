package co.wethinkcode.logisticsconnect.repository;

import co.wethinkcode.logisticsconnect.model.entity.Hub;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.net.http.HttpClient;

public class HubRepository {

    private final LinkedHashMap<String, Hub> hubs;
    private final String BASE_URL = "http://localhost:7051/hubs";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client;

    public HubRepository() throws IOException, InterruptedException {
            this.client = HttpClient.newHttpClient();
            this.hubs = new LinkedHashMap<>();

            for(Hub hub: getAll()) {
                hubs.put(hub.getHubId(), hub);
            }
    }

    public void updateStage(String hubId, int stage) {
        Hub hub = hubs.get(hubId);
        if (hub != null) {
            hub.setStage(stage);
        }
    }

    public Hub getByHubId(String hubId) {
        return hubs.get(hubId);
    }

    public List<Hub> getAllActive() {
        List<Hub> allActive = new ArrayList<>();

        for (Hub hub : this.hubs.values()) {
            if (hub.isActive()) {
                allActive.add(hub);
            }
        }

        return allActive;
    }

    public List<Hub> getAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(
                response.body(),
                new TypeReference<List<Hub>>() {}
        );
    }
}
