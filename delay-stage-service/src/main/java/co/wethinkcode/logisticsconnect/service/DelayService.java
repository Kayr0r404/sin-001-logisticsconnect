package co.wethinkcode.logisticsconnect.service;

import co.wethinkcode.logisticsconnect.mapper.HubMapper;
import co.wethinkcode.logisticsconnect.model.dto.*;
import co.wethinkcode.logisticsconnect.model.entity.Hub;
import co.wethinkcode.logisticsconnect.repository.HubRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;

import java.io.IOException;
import java.util.List;

public class DelayService {

    private final HubRepository repository;

    public DelayService(HubRepository repository) {
        this.repository = repository;
    }

//    public List<String> getAll() throws IOException, InterruptedException {
//        return repository.getAll().stream()
//                .map(HubMapper::toDto)
//                .toList();
//    }
//
//    public List<String> getAllActive() throws JsonProcessingException{
//        return repository.getAllActive().stream()
//                .map(HubMapper::toDto)
//                .toList();
//    }

    public String getByHubId(String id) throws JsonProcessingException {
        Hub result = repository.getByHubId(id);

        return HubMapper.toDto(result);
    }

    public void save(String request) throws JsonProcessingException {
        repository.save(HubMapper.fromDto(request));
    }

}