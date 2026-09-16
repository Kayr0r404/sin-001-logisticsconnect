package co.wethinkcode.logisticsconnect.service;

import co.wethinkcode.logisticsconnect.mapper.IngestionMapper;
import co.wethinkcode.logisticsconnect.model.dto.IngestionRequest;
import co.wethinkcode.logisticsconnect.model.dto.IngestionResponse;
import co.wethinkcode.logisticsconnect.model.entity.Ingestion;
import co.wethinkcode.logisticsconnect.repository.IngestionRepository;

import java.util.List;

public class IngestionService {

    private final IngestionRepository repository;

    public IngestionService(IngestionRepository repository) {
        this.repository = repository;
    }

    public List<IngestionResponse> getAll() {
        return repository.getAll().stream()
                .map(IngestionMapper::toDto)
                .toList();
    }

    public List<IngestionResponse> getAllActive() {
        return repository.getAllActive().stream()
                .map(IngestionMapper::toDto)
                .toList();
    }

    public List<IngestionResponse> getByProvince(String province) {

        List<Ingestion> records = repository.getByProvince(province);

        return records.stream()
                .map(record -> new IngestionResponse(
                        record.getHubId(),
                        record.getProvince(),
                        record.getSortingCenter(),
                        record.isActive()
                ))
                .toList();
    }

    public List<IngestionResponse> getBySortingCenter(String center) {
        List<Ingestion> records = repository.getBySortingCenter(center);

        return records.stream()
                .map(record -> new IngestionResponse(
                        record.getHubId(),
                        record.getProvince(),
                        record.getSortingCenter(),
                        record.isActive()
                ))
                .toList();
    }

    public IngestionResponse getByHubId(String id) {
        Ingestion result = repository.getByHubId(id);

        return IngestionMapper.toDto(result);
    }

    public void save(IngestionRequest request) {
        repository.save(IngestionMapper.fromDto(request));
    }

}