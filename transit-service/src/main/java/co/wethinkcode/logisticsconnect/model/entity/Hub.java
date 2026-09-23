package co.wethinkcode.logisticsconnect.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Hub {
    private String hubId;
    private String province;
    private  String sortingCenter;
    private int stage;
    @JsonProperty("isActive")
    private boolean isActive;
    private String timestamp;

    public Hub() {
    }

    public Hub(String hubId, String province, String sortingCenter, int stage,boolean isActive, String timestamp) {
        this.hubId = hubId;
        this.province = province;
        this.sortingCenter = sortingCenter;
        this.isActive = isActive;
        this.stage = stage;
        this.timestamp = timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String time) {
        this.timestamp = time;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hub hub = (Hub) o;
        return stage == hub.stage && isActive == hub.isActive && Objects.equals(hubId, hub.hubId) && Objects.equals(province, hub.province) && Objects.equals(sortingCenter, hub.sortingCenter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hubId, province, sortingCenter, stage, isActive);
    }

    @Override
    public String toString() {
        return "Hub{" +
                "hubId='" + hubId + '\'' +
                ", province='" + province + '\'' +
                ", sortingCenter='" + sortingCenter + '\'' +
                ", stage=" + stage +
                ", isActive=" + isActive +
                '}';
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}