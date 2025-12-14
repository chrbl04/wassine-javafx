// model/ServiceApproval.java
package model;
import java.time.LocalDateTime;

public class ServiceApproval {
    private int adminId, serviceId, senderId;
    private LocalDateTime approvedAt;
    public ServiceApproval() {}
    public ServiceApproval(int adminId, int serviceId, int senderId, LocalDateTime approvedAt) {
        this.adminId = adminId; this.serviceId = serviceId; this.senderId = senderId; this.approvedAt = approvedAt;
    }
    // getters/setters...
    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
}
