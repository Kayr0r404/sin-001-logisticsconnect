package co.wethinkcode.logisticsconnect.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Hub {
    private String hubId;
    private String province;
    private  String sortingCenter;
    @JsonProperty("isActive")
    private boolean isActive;

    public Hub() {
    }

    public Hub(String hubId, String province, String sortingCenter, boolean isActive) {
        this.hubId = hubId;
        this.province = province;
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
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Hub ingestion = (Hub) object;
        return isActive == ingestion.isActive && java.util.Objects.equals(hubId, ingestion.hubId) && java.util.Objects.equals(province, ingestion.province) && java.util.Objects.equals(sortingCenter, ingestion.sortingCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hubId, province, sortingCenter, isActive);
    }

    @Override
    public java.lang.String toString() {
        return "Ingestion{" +
                ", hubId='" + hubId + '\'' +
                ", Province='" + province + '\'' +
                ", sortingCenter='" + sortingCenter + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}