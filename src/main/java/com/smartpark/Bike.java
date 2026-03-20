package com.smartpark;

public class Bike extends Vehicle {

    public Bike(String vehicleId, String licensePlate, String ownerId) {
        super(vehicleId, licensePlate, ownerId);
    }

    @Override
    public String getVehicleType() {
        return "Bike";
    }

    @Override
    public double getParkingRate() {
        return 10.0; // Rs 10 per hour
    }
}
