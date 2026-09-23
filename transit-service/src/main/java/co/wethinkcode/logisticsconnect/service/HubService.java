package co.wethinkcode.logisticsconnect.service;

import co.wethinkcode.logisticsconnect.model.dto.HubResponse;
import co.wethinkcode.logisticsconnect.model.entity.Hub;
import co.wethinkcode.logisticsconnect.repository.HubRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class HubService {

    private static final long BASE_TRANSIT_MINUTES = 120;
    private static final long DELAY_MINUTES_PER_STAGE = 30;

    private final HubRepository repository;

    public HubService(HubRepository repository) {
        this.repository = repository;
    }

    /**
     * Estimated arrival = now + base transit time + per-stage delay.
     * Stage 0 is on time; each extra delay stage adds DELAY_MINUTES_PER_STAGE.
     */
    public Optional<HubResponse> calculateEta(String hubId, int delayStage) {
        Hub hub = repository.getByHubId(hubId);
        if (hub == null) {
            return Optional.empty();
        }

        long minutes = BASE_TRANSIT_MINUTES + Math.max(delayStage, 0) * DELAY_MINUTES_PER_STAGE;
        String estimatedTimeArrival = Instant.now().plus(Duration.ofMinutes(minutes)).toString();

        return Optional.of(new HubResponse(hubId, estimatedTimeArrival));
    }

    public void updateStage(String hubId, int stage) {
        repository.updateStage(hubId, stage);
    }
}
