package com.smartpark.smartpark.models;

public class Truck extends Vehicle {

    private double loadCapacityTons;

    public Truck(String licensePlate, String ownerName, String ownerPhone, double loadCapacityTons) {
        super(licensePlate, ownerName, ownerPhone);
        this.loadCapacityTons = loadCapacityTons;
    }

    public Truck() {}

    @Override
    public String getVehicleType() {
        return "Truck";
    }

    @Override
    public double getParkingRate() {
        return loadCapacityTons > 5 ? 100.0 : 70.0; // Heavy trucks cost more
    }

    public double getLoadCapacityTons() { return loadCapacityTons; }
    public void setLoadCapacityTons(double loadCapacityTons) { this.loadCapacityTons = loadCapacityTons; }
}