package com.smartpark.models;

public class Bill {

    private String vehicleType;
    private double hoursParked;
    private double ratePerHour;
    private double totalAmount;

    public Bill(String vehicleType, double hoursParked, double ratePerHour, double totalAmount) {
        this.vehicleType = vehicleType;
        this.hoursParked = hoursParked;
        this.ratePerHour = ratePerHour;
        this.totalAmount = totalAmount;
    }

    public String getVehicleType() { return vehicleType; }
    public double getHoursParked() { return hoursParked; }
    public double getRatePerHour() { return ratePerHour; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Vehicle: " + vehicleType +
               " | Hours: " + hoursParked +
               " | Rate: Rs." + ratePerHour + "/hr" +
               " | Total: Rs." + totalAmount;
    }
}
