package com.smartpark.service;

import com.smartpark.models.Bill;
import com.smartpark.models.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

public class BillingService {

    public Bill calculateBill(Vehicle vehicle, LocalDateTime checkIn, LocalDateTime checkOut) {

        // Calculate hours parked
        long minutes = Duration.between(checkIn, checkOut).toMinutes();
        double hoursParked = Math.ceil(minutes / 60.0);

        // Polymorphism — each vehicle returns its own rate
        double rate = vehicle.getParkingRate();
        double total = vehicle.calculateBill(hoursParked);

        return new Bill(
            vehicle.getVehicleType(),
            hoursParked,
            rate,
            total
        );
    }
}
