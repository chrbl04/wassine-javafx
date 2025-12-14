package model;

import java.time.LocalDateTime;

public class Accept {

    private int serviceId;
    private int travelerId;
    private LocalDateTime acceptanceDate;

    public Accept() {}

    public Accept(int serviceId, int travelerId, LocalDateTime acceptanceDate) {
        this.serviceId = serviceId;
        this.travelerId = travelerId;
        this.acceptanceDate = acceptanceDate;
    }

    // ======== GETTERS ========

    public int getServiceId() {
        return serviceId;
    }

    public int getTravelerId() {
        return travelerId;
    }

    public LocalDateTime getAcceptanceDate() {
        return acceptanceDate;
    }

    // ======== SETTERS ========

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setTravelerId(int travelerId) {
        this.travelerId = travelerId;
    }

    public void setAcceptanceDate(LocalDateTime acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    @Override
    public String toString() {
        return "Accept{" +
                "serviceId=" + serviceId +
                ", travelerId=" + travelerId +
                ", acceptanceDate=" + acceptanceDate +
                '}';
    }
}
