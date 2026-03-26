package com.smartpark.smartpark.utils;

import com.smartpark.smartpark.models.Vehicle;
import com.smartpark.smartpark.models.Car;
import com.smartpark.smartpark.models.Bike;
import com.smartpark.smartpark.models.Truck;

public class PolymorphismDemo {

    public static void main(String[] args) {

        // Polymorphism - one array holds all vehicle types
        Vehicle[] vehicles = {
            new Car("PB01-1234", "Harnoor", "9876543210", false),
            new Bike("PB02-5678", "Himani", "9876543211", false),
            new Truck("PB03-9999", "Manisha", "9876543212", 8.0),
            new Car("PB04-1111", "Aditi", "9876543213", true)
        };

        System.out.println("=== SmartPark Vehicle Rates ===");
        for (Vehicle v : vehicles) {
            System.out.println(v.toString() + " | Rate: ₹" + v.getParkingRate() + "/hr");
        }
    }
}