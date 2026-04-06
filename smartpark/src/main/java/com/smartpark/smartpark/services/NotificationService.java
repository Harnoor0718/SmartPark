package com.smartpark.smartpark.services;

import com.smartpark.smartpark.interfaces.Notifiable;

public class NotificationService implements Notifiable {

    @Override
    public void sendEmailAlert(String to, String subject, String body) {
        // Stub implementation - ready for Phase 2
        System.out.println("=== EMAIL ALERT ===");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("==================");
    }

    @Override
    public void sendSMSAlert(String phone, String message) {
        // Stub implementation - ready for Phase 2
        System.out.println("=== SMS ALERT ===");
        System.out.println("Phone: " + phone);
        System.out.println("Message: " + message);
        System.out.println("=================");
    }

    // Helper method - send booking confirmation
    public void sendBookingConfirmation(String email, String phone, int bookingId, String vehiclePlate) {
        sendEmailAlert(
            email,
            "SmartPark Booking Confirmation",
            "Your booking #" + bookingId + " for vehicle " + vehiclePlate + " is confirmed!"
        );
        sendSMSAlert(
            phone,
            "SmartPark: Booking #" + bookingId + " confirmed for " + vehiclePlate
        );
    }

    // Helper method - send checkout receipt
    public void sendCheckoutReceipt(String email, int bookingId, double amount) {
        sendEmailAlert(
            email,
            "SmartPark Checkout Receipt",
            "Booking #" + bookingId + " completed. Total amount: Rs." + amount
        );
    }
}