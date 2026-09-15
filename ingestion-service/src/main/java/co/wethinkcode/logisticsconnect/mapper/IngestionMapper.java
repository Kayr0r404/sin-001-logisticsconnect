package co.wethinkcode.logisticsconnect.mapper;

public class IngestionMapper {

    public static IngestionDto toDto(Ingestion ingestion) {
        return new UserDto(
                ingestion.getId(),
                ingestion.getHubId(),
                ingestion.getProvince(),
                ingestion.getSortingCenter(),
                igestion.isActive()
        );

    }

    public static Ingestion fromDto(IngestionDto ingestionDto) {
        return new Ingetion(
                id=ingestionDto.id,
                hubId = ingestionDto.hubId,
                province = ingestionDto.province,
                sortingCenter = ingestionDto.sortingCenter,
                isActive = ingestionDto.isActive
        );
    }
}