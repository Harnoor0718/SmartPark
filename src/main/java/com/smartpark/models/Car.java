package com.smartpark.models;

import com.smartpark.interfaces.Payable;

public class Car extends Vehicle implements Payable {

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

    @Override
    public double calculateBill(double hours) {
        return hours * getParkingRate();
    }

    @Override
    public void printReceipt(double hours) {
        System.out.println("--- SmartPark Receipt ---");
        System.out.println("Vehicle Type : " + getVehicleType());
        System.out.println("License Plate: " + getLicensePlate());
        System.out.println("Hours Parked : " + hours);
        System.out.println("Rate per Hour: Rs " + getParkingRate());
        System.out.println("Total Bill   : Rs " + calculateBill(hours));
        System.out.println("-------------------------");
    }
}