// model/Vehicle.java
package model;

public class Vehicle {
    public enum VehicleType { Car, Plane, Boat, Other }
    public enum Status { Available, Under_Maintenance, Inactive, Unavailable }

    private int vehicleId;
    private int travelerId;
    private VehicleType vehicleType;
    private String make, model, vehicleNumber;
    private Double capacityWeight;      // DECIMAL(8,2) -> Double
    private Status status;

    public Vehicle() {}
    public Vehicle(int vehicleId, int travelerId, VehicleType type, String make, String model,
                   String vehicleNumber, Double capacityWeight, Status status) {
        this.vehicleId = vehicleId; this.travelerId = travelerId; this.vehicleType = type;
        this.make = make; this.model = model; this.vehicleNumber = vehicleNumber;
        this.capacityWeight = capacityWeight; this.status = status;
    }
    // getters/setters...
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getTravelerId() { return travelerId; }
    public void setTravelerId(int travelerId) { this.travelerId = travelerId; }
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public Double getCapacityWeight() { return capacityWeight; }
    public void setCapacityWeight(Double capacityWeight) { this.capacityWeight = capacityWeight; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override public String toString() {
        return "Vehicle{" + vehicleId + ", traveler=" + travelerId + ", " + vehicleType +
                ", " + make + " " + model + ", cap=" + capacityWeight + ", " + status + "}";
    }
}
