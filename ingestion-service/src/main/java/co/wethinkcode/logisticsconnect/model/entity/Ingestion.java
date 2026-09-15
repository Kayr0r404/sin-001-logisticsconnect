package co.wethinkcode.logisticsconnect.model.entity;

public class Ingestion {
    private UUID id;
    private String hubId;
    private String Province;
    private  String sortingCenter;
    private boolean isActive;

    public Ingestion() {
    }

    public Ingestion(UUID id, String hubId, String province, String sortingCenter, boolean isActive) {
        this.id = id;
        this.hubId = hubId;
        Province = province;
        this.sortingCenter = sortingCenter;
        this.isActive = isActive;
    }

    public String getHubId() {
        return hubId;
    }

    public void setHubId(String hubId) {
        this.hubId = hubId;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getSortingCenter() {
        return sortingCenter;
    }

    public void setSortingCenter(String sortingCenter) {
        this.sortingCenter = sortingCenter;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Ingestion ingestion = (Ingestion) object;
        return isActive == ingestion.isActive && java.util.Objects.equals(id, ingestion.id) && java.util.Objects.equals(hubId, ingestion.hubId) && java.util.Objects.equals(Province, ingestion.Province) && java.util.Objects.equals(sortingCenter, ingestion.sortingCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, hubId, Province, sortingCenter, isActive);
    }

    @Override
    public java.lang.String toString() {
        return "Ingestion{" +
                "id=" + id +
                ", hubId='" + hubId + '\'' +
                ", Province='" + Province + '\'' +
                ", sortingCenter='" + sortingCenter + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}