package com.smartpark.service;

import com.smartpark.models.Bill;
import com.smartpark.models.Invoice;
import com.smartpark.models.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public class CheckOutService {

    private BillingService billingService = new BillingService();

    public Invoice processCheckOut(Vehicle vehicle, int bookingId,
                                   LocalDateTime checkIn, LocalDateTime checkOut) {

        Bill bill = billingService.calculateBill(vehicle, checkIn, checkOut);

        String invoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Invoice invoice = new Invoice(
            invoiceId,
            bookingId,
            bill.getVehicleType(),
            bill.getHoursParked(),
            bill.getRatePerHour(),
            bill.getTotalAmount()
        );

        System.out.println(invoice);

        return invoice;
    }
}
