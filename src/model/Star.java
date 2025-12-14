package model;

import java.time.LocalDateTime;

public class Star {

    private int serviceId;
    private int travelerId;
    private LocalDateTime starDate;

    public Star() {}

    public Star(int serviceId, int travelerId, LocalDateTime starDate) {
        this.serviceId = serviceId;
        this.travelerId = travelerId;
        this.starDate = starDate;
    }

    // ======== GETTERS ========

    public int getServiceId() {
        return serviceId;
    }

    public int getTravelerId() {
        return travelerId;
    }

    public LocalDateTime getStarDate() {
        return starDate;
    }

    // ======== SETTERS ========

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setTravelerId(int travelerId) {
        this.travelerId = travelerId;
    }

    public void setStarDate(LocalDateTime starDate) {
        this.starDate = starDate;
    }

    @Override
    public String toString() {
        return "Star{" +
                "serviceId=" + serviceId +
                ", travelerId=" + travelerId +
                ", starDate=" + starDate +
                '}';
    }
}

