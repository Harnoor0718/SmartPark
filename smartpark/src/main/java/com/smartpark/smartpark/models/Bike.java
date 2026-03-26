package com.smartpark.smartpark.models;

public class Bike extends Vehicle {

    private boolean isHeavyBike;

    public Bike(String licensePlate, String ownerName, String ownerPhone, boolean isHeavyBike) {
        super(licensePlate, ownerName, ownerPhone);
        this.isHeavyBike = isHeavyBike;
    }

    public Bike() {}

    @Override
    public String getVehicleType() {
        return isHeavyBike ? "Heavy Bike" : "Bike";
    }

    @Override
    public double getParkingRate() {
        return isHeavyBike ? 30.0 : 20.0;
    }

    public boolean isHeavyBike() { return isHeavyBike; }
    public void setHeavyBike(boolean heavyBike) { isHeavyBike = heavyBike; }
}