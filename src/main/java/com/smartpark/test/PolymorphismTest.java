package com.smartpark.test;

import com.smartpark.models.Bike;
import com.smartpark.models.Bill;
import com.smartpark.models.Car;
import com.smartpark.models.Truck;
import com.smartpark.models.Vehicle;
import com.smartpark.service.BillingService;

import java.time.LocalDateTime;

public class PolymorphismTest {

    public static void main(String[] args) {

        BillingService billing = new BillingService();

        // Single array with 3 different vehicle types
        Vehicle[] vehicles = {
            new Car("V001", "MH01AA0001", "Aditi"),
            new Bike("V002", "MH01BB0002", "Aditi"),
            new Truck("V003", "MH01CC0003", "Aditi")
        };

        LocalDateTime checkIn  = LocalDateTime.of(2025, 6, 1, 9, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 6, 1, 11, 0);

        System.out.println("===== Polymorphism Demo =====");

        // Single loop — polymorphism gives different bill for each vehicle
        for (Vehicle v : vehicles) {
            Bill bill = billing.calculateBill(v, checkIn, checkOut);
            System.out.println(bill);
        }

        System.out.println("=============================");
    }
}
