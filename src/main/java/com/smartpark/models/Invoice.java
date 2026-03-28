package com.smartpark.models;

import java.time.LocalDateTime;

public class Invoice {

    private String invoiceId;
    private int bookingId;
    private String vehicleType;
    private double hoursParked;
    private double ratePerHour;
    private double totalAmount;
    private LocalDateTime generatedAt;

    public Invoice(String invoiceId, int bookingId, String vehicleType,
                   double hoursParked, double ratePerHour, double totalAmount) {
        this.invoiceId = invoiceId;
        this.bookingId = bookingId;
        this.vehicleType = vehicleType;
        this.hoursParked = hoursParked;
        this.ratePerHour = ratePerHour;
        this.totalAmount = totalAmount;
        this.generatedAt = LocalDateTime.now();
    }

    public String getInvoiceId() { return invoiceId; }
    public int getBookingId() { return bookingId; }
    public String getVehicleType() { return vehicleType; }
    public double getHoursParked() { return hoursParked; }
    public double getRatePerHour() { return ratePerHour; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }

    @Override
    public String toString() {
        return "\n===== SmartPark Invoice =====\n" +
               "Invoice ID  : " + invoiceId + "\n" +
               "Booking ID  : " + bookingId + "\n" +
               "Vehicle Type: " + vehicleType + "\n" +
               "Hours Parked: " + hoursParked + " hrs\n" +
               "Rate        : Rs." + ratePerHour + "/hr\n" +
               "Total Amount: Rs." + totalAmount + "\n" +
               "Generated At: " + generatedAt + "\n" +
               "=============================";
    }
}
