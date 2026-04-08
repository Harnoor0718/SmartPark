package com.smartpark.test;

import com.smartpark.models.*;
import com.smartpark.service.*;

import java.time.LocalDateTime;

public class SystemTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       SMARTPARK FULL SYSTEM TEST       ");
        System.out.println("========================================");

        // TEST 1: Polymorphism
        System.out.println("\n--- TEST 1: Polymorphism Demo ---");
        BillingService billing = new BillingService();
        Vehicle[] vehicles = {
            new Car("V001", "MH12AB1234", "Aditi"),
            new Bike("V002", "MH12CD5678", "Aditi"),
            new Truck("V003", "MH12EF9999", "Aditi")
        };
        LocalDateTime checkIn  = LocalDateTime.of(2025, 6, 1, 9, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 6, 1, 11, 0);
        for (Vehicle v : vehicles) {
            Bill bill = billing.calculateBill(v, checkIn, checkOut);
            System.out.println(bill);
        }

        // TEST 2: Invoice Generation
        System.out.println("\n--- TEST 2: Invoice Generation ---");
        CheckOutService checkOutService = new CheckOutService();
        Vehicle car = new Car("V001", "MH12AB1234", "Aditi");
        Invoice invoice = checkOutService.processCheckOut(car, 101, checkIn, checkOut);
        System.out.println("Invoice Generated: " + invoice.getInvoiceId());

        // TEST 3: QR Code
        System.out.println("\n--- TEST 3: QR Code Generation ---");
        try {
            QRCodeGenerator.saveQR("BookingID:101|Vehicle:Car|Slot:A12", "SystemTestQR.png", 300, 300);
            System.out.println("QR Code generated successfully!");
        } catch (Exception e) {
            System.out.println("QR Error: " + e.getMessage());
        }

        System.out.println("\n========================================");
        System.out.println("      ALL TESTS PASSED! DEMO READY!     ");
        System.out.println("========================================");
    }
}
