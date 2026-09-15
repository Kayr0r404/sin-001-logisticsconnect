package co.wethinkcode.logisticsconnect.model.dto;

public record IngestionDto(
        UUID id,
        String hubId,
        String Province ,
        String sortingCenter,
        boolean isActive
){

}