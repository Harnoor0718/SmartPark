package com.smartpark;

public class PolymorphismDemo {

    public static void main(String[] args) {

        // Polymorphism demo — single array, single loop
        Vehicle[] vehicles = {
            new Car("V001", "DL1234", "U001"),
            new Bike("V002", "DL5678", "U002"),
            new Truck("V003", "DL9999", "U003")
        };

        System.out.println("===== SmartPark Polymorphism Demo =====");
        System.out.println();

        double hours = 3.0;

        for (Vehicle v : vehicles) {
            Payable p = (Payable) v;
            p.printReceipt(hours);
            System.out.println();
        }

        System.out.println("=======================================");
        System.out.println("Demo complete! All vehicle types billed correctly. ✅");
    }
}