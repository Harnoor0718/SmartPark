package com.smartpark.models;

public abstract class Vehicle {

    private String vehicleId;
    private String licensePlate;
    private String ownerId;

    public Vehicle(String vehicleId, String licensePlate, String ownerId) {
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.ownerId = ownerId;
    }

    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getLicensePlate() { return licensePlate; }
    public String getOwnerId() { return ownerId; }

    // Abstract methods — each subclass must implement these
    public abstract String getVehicleType();
    public abstract double getParkingRate();
}