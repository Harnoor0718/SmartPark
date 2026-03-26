package com.smartpark.smartpark.models;

import java.time.LocalDateTime;

public class Payment implements com.smartpark.smartpark.interfaces.Payable {

    public enum PaymentMethod {
        CASH, CARD, UPI
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    private int id;
    private int bookingId;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
    private double hoursParked;

    public Payment(int bookingId, double amount, PaymentMethod paymentMethod, double hoursParked) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.hoursParked = hoursParked;
    }

    public Payment() {}

    @Override
    public double calculateBill(double hoursParked) {
        return hoursParked * amount;
    }

    @Override
    public void printReceipt() {
        System.out.println("=== SmartPark Receipt ===");
        System.out.println("Booking ID : " + bookingId);
        System.out.println("Hours      : " + hoursParked);
        System.out.println("Amount     : Rs." + amount);
        System.out.println("Method     : " + paymentMethod);
        System.out.println("Status     : " + paymentStatus);
        System.out.println("Time       : " + paymentTime);
        System.out.println("========================");
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDateTime paymentTime) { this.paymentTime = paymentTime; }

    public double getHoursParked() { return hoursParked; }
    public void setHoursParked(double hoursParked) { this.hoursParked = hoursParked; }
}