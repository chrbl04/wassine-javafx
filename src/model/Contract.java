package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Contract {
    public enum PaymentMethod { Cash, Card, Wallet }
    public enum Payment { Pending, Paid, Refunded }
    public enum Status { Active, Completed, Cancelled, Disputed }

    private int contractId;
    private int vehicleId;
    private LocalDateTime createdAt;
    private BigDecimal transportFee;
    private LocalDateTime pickupDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime signedDate;
    private String termsText;
    private PaymentMethod paymentMethod;
    private Payment payment;
    private Status status;

    public Contract() {}

    public Contract(int contractId, int vehicleId, LocalDateTime createdAt, BigDecimal transportFee,
                    LocalDateTime pickupDate, LocalDateTime deliveryDate, LocalDateTime signedDate,
                    String termsText, PaymentMethod paymentMethod, Payment payment, Status status) {
        this.contractId = contractId;
        this.vehicleId = vehicleId;
        this.createdAt = createdAt;
        this.transportFee = transportFee;
        this.pickupDate = pickupDate;
        this.deliveryDate = deliveryDate;
        this.signedDate = signedDate;
        this.termsText = termsText;
        this.paymentMethod = paymentMethod;
        this.payment = payment;
        this.status = status;
    }

    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public BigDecimal getTransportFee() { return transportFee; }
    public void setTransportFee(BigDecimal transportFee) { this.transportFee = transportFee; }
    public LocalDateTime getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }
    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }
    public LocalDateTime getSignedDate() { return signedDate; }
    public void setSignedDate(LocalDateTime signedDate) { this.signedDate = signedDate; }
    public String getTermsText() { return termsText; }
    public void setTermsText(String termsText) { this.termsText = termsText; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override public String toString() {
        return "Contract{" +
                "id=" + contractId +
                ", vehicleId=" + vehicleId +
                ", fee=" + transportFee +
                ", status=" + status +
                ", payment=" + payment +
                ", method=" + paymentMethod +
                ", createdAt=" + createdAt +
                '}';
    }
}
