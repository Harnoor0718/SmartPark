package com.smartpark.test;

import com.smartpark.models.Car;
import com.smartpark.models.Bike;
import com.smartpark.models.Truck;
import com.smartpark.models.Vehicle;
import com.smartpark.models.Invoice;
import com.smartpark.service.CheckOutService;

import java.time.LocalDateTime;

public class PaymentTest {

    public static void main(String[] args) {

        CheckOutService checkOutService = new CheckOutService();

        // Test Car - Rs.20/hr x 2hrs = Rs.40
        Vehicle car = new Car("V001", "MH12AB1234", "Aditi");
        Invoice carInvoice = checkOutService.processCheckOut(
            car, 101,
            LocalDateTime.of(2025, 6, 1, 9, 0),
            LocalDateTime.of(2025, 6, 1, 11, 0)
        );

        // Test Bike - Rs.10/hr x 2hrs = Rs.20
        Vehicle bike = new Bike("V002", "MH12CD5678", "Aditi");
        Invoice bikeInvoice = checkOutService.processCheckOut(
            bike, 102,
            LocalDateTime.of(2025, 6, 1, 10, 0),
            LocalDateTime.of(2025, 6, 1, 12, 0)
        );

        // Test Truck - Rs.35/hr x 2hrs = Rs.70
        Vehicle truck = new Truck("V003", "MH12EF9999", "Aditi");
        Invoice truckInvoice = checkOutService.processCheckOut(
            truck, 103,
            LocalDateTime.of(2025, 6, 1, 8, 0),
            LocalDateTime.of(2025, 6, 1, 10, 0)
        );

        // Summary
        System.out.println("===== Payment Summary =====");
        System.out.println("Car Total   : Rs." + carInvoice.getTotalAmount());
        System.out.println("Bike Total  : Rs." + bikeInvoice.getTotalAmount());
        System.out.println("Truck Total : Rs." + truckInvoice.getTotalAmount());
    }
}
