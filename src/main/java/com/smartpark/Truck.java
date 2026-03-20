package com.smartpark;

public class Truck extends Vehicle {

    public Truck(String vehicleId, String licensePlate, String ownerId) {
        super(vehicleId, licensePlate, ownerId);
    }

    @Override
    public String getVehicleType() {
        return "Truck";
    }

    @Override
    public double getParkingRate() {
        return 35.0; // Rs 35 per hour
    }
}