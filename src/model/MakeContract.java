package model;

public class MakeContract {

    private int serviceId;
    private int senderId;
    private int travelerId;
    private int adminId;
    private int contractId;

    public MakeContract() {}

    public MakeContract(int serviceId, int senderId, int travelerId,
                        int adminId, int contractId) {
        this.serviceId = serviceId;
        this.senderId = senderId;
        this.travelerId = travelerId;
        this.adminId = adminId;
        this.contractId = contractId;
    }

    // ======== GETTERS ========

    public int getServiceId() {
        return serviceId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getTravelerId() {
        return travelerId;
    }

    public int getAdminId() {
        return adminId;
    }

    public int getContractId() {
        return contractId;
    }

    // ======== SETTERS ========

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setTravelerId(int travelerId) {
        this.travelerId = travelerId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    // Optional: helps when printing/debugging
    @Override
    public String toString() {
        return "MakeContract{" +
                "serviceId=" + serviceId +
                ", senderId=" + senderId +
                ", travelerId=" + travelerId +
                ", adminId=" + adminId +
                ", contractId=" + contractId +
                '}';
    }
}
