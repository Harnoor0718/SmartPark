package com.smartpark.smartpark.models;

public class Car extends Vehicle {

    private boolean isElectric;

    public Car(String licensePlate, String ownerName, String ownerPhone, boolean isElectric) {
        super(licensePlate, ownerName, ownerPhone);
        this.isElectric = isElectric;
    }

    public Car() {}

    @Override
    public String getVehicleType() {
        return "Car";
    }

    @Override
    public double getParkingRate() {
        return isElectric ? 30.0 : 50.0; // Electric cars get discount
    }

    public boolean isElectric() { return isElectric; }
    public void setElectric(boolean electric) { isElectric = electric; }
}