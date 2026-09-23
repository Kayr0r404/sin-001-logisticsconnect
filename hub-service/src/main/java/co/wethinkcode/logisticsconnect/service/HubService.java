package co.wethinkcode.logisticsconnect.service;

import co.wethinkcode.logisticsconnect.mapper.HubMapper;
import co.wethinkcode.logisticsconnect.model.dto.*;
import co.wethinkcode.logisticsconnect.model.entity.Hub;
import co.wethinkcode.logisticsconnect.repository.HubRepository;

import java.io.IOException;
import java.util.List;

public class HubService {

    private final HubRepository repository;

    public HubService(HubRepository repository) {
        this.repository = repository;
    }

    public List<HubResponse> getAll() throws IOException, InterruptedException {
        return repository.getAll().stream()
                .map(HubMapper::toDto)
                .toList();
    }

    public List<HubResponse> getAllActive() {
        return repository.getAllActive().stream()
                .map(HubMapper::toDto)
                .toList();
    }

    public List<HubResponse> getByProvince(String province) {

        List<Hub> records = repository.getByProvince(province);

        return records.stream()
                .map(record -> new HubResponse(
                        record.getHubId(),
                        record.getProvince(),
                        record.getSortingCenter(),
                        record.getStage(),
                        record.isActive()
                ))
                .toList();
    }

    public List<HubResponse> getBySortingCenter(String center) {
        List<Hub> records = repository.getBySortingCenter(center);

        return records.stream()
                .map(record -> new HubResponse(
                        record.getHubId(),
                        record.getProvince(),
                        record.getSortingCenter(),
                        record.getStage(),
                        record.isActive()
                ))
                .toList();
    }

    public HubResponse getByHubId(String id) {
        Hub result = repository.getByHubId(id);

        return HubMapper.toDto(result);
    }

    public void save(HubRequest request) {
        repository.save(HubMapper.fromDto(request));
    }

}