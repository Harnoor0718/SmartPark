package com.smartpark.smartpark.models;

public abstract class Vehicle {

    private String licensePlate;
    private String ownerName;
    private String ownerPhone;

    // Constructor
    public Vehicle(String licensePlate, String ownerName, String ownerPhone) {
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
    }

    // Empty constructor
    public Vehicle() {}

    // Abstract methods - every subclass MUST implement these
    public abstract String getVehicleType();
    public abstract double getParkingRate(); // rate per hour

    // Getters and Setters
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }

    @Override
    public String toString() {
        return getVehicleType() + " | Plate: " + licensePlate + " | Owner: " + ownerName;
    }
}