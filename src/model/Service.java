package model;

import java.time.LocalDateTime;

public class Service {
    private int serviceId;
    private String title;
    private String description;
    private double parcelWeightKg;
    private String category;
    private String pickupAddress;
    private String dropoffAddress;
    private LocalDateTime pickupTimeFrom;
    private LocalDateTime pickupTimeUntil;
    private LocalDateTime deliveryDeadline;
    private boolean insuranceRequested;
    private double offerPrice;
    private String status;
    private LocalDateTime createdAt;

    public Service() {}

    public Service(int serviceId, String title, String description, double parcelWeightKg, String category,
                   String pickupAddress, String dropoffAddress, LocalDateTime pickupTimeFrom,
                   LocalDateTime pickupTimeUntil, LocalDateTime deliveryDeadline, boolean insuranceRequested,
                   double offerPrice, String status, LocalDateTime createdAt) {
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.parcelWeightKg = parcelWeightKg;
        this.category = category;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.pickupTimeFrom = pickupTimeFrom;
        this.pickupTimeUntil = pickupTimeUntil;
        this.deliveryDeadline = deliveryDeadline;
        this.insuranceRequested = insuranceRequested;
        this.offerPrice = offerPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getParcelWeightKg() { return parcelWeightKg; }
    public void setParcelWeightKg(double parcelWeightKg) { this.parcelWeightKg = parcelWeightKg; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDropoffAddress() { return dropoffAddress; }
    public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }

    public LocalDateTime getPickupTimeFrom() { return pickupTimeFrom; }
    public void setPickupTimeFrom(LocalDateTime pickupTimeFrom) { this.pickupTimeFrom = pickupTimeFrom; }

    public LocalDateTime getPickupTimeUntil() { return pickupTimeUntil; }
    public void setPickupTimeUntil(LocalDateTime pickupTimeUntil) { this.pickupTimeUntil = pickupTimeUntil; }

    public LocalDateTime getDeliveryDeadline() { return deliveryDeadline; }
    public void setDeliveryDeadline(LocalDateTime deliveryDeadline) { this.deliveryDeadline = deliveryDeadline; }

    public boolean isInsuranceRequested() { return insuranceRequested; }
    public void setInsuranceRequested(boolean insuranceRequested) { this.insuranceRequested = insuranceRequested; }

    public double getOfferPrice() { return offerPrice; }
    public void setOfferPrice(double offerPrice) { this.offerPrice = offerPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", title='" + title + '\'' +
                ", parcelWeightKg=" + parcelWeightKg +
                ", category='" + category + '\'' +
                ", pickupAddress='" + pickupAddress + '\'' +
                ", dropoffAddress='" + dropoffAddress + '\'' +
                ", offerPrice=" + offerPrice +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
