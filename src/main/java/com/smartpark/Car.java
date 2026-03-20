package com.smartpark;

public class Car extends Vehicle {

    public Car(String vehicleId, String licensePlate, String ownerId) {
        super(vehicleId, licensePlate, ownerId);
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }

    @Override
    public double getParkingRate() {
        return 20.0; // Rs 20 per hour
    }
}