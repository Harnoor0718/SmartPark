package com.smartpark.dao;

public class FullFlowTest {

    public static void main(String[] args) {

        System.out.println("=== SMARTPARK FULL FLOW TEST ===\n");

        VehicleDAO vehicleDAO = new VehicleDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        // Step 1 - Save a vehicle
        System.out.println("--- Step 1: Save Vehicle ---");
        // Note: User U001 must already exist in users table
        // Run this in MySQL first:
        // INSERT INTO users VALUES ('U001','Manisha','manisha@gmail.com','pass123','USER');

        // Step 2 - Save a booking
        System.out.println("\n--- Step 2: Create Booking ---");
        bookingDAO.save("B001", "V001", "S001", "U001");

        // Step 3 - Find the booking
        System.out.println("\n--- Step 3: Find Booking ---");
        bookingDAO.findById("B001");

        // Step 4 - Save a payment
        System.out.println("\n--- Step 4: Save Payment ---");
        paymentDAO.save("P001", "B001", 150.00, "COMPLETED");

        // Step 5 - Find payment by booking
        System.out.println("\n--- Step 5: Find Payment ---");
        paymentDAO.findByBookingId("B001");

        // Step 6 - Update booking to completed
        System.out.println("\n--- Step 6: Checkout ---");
        bookingDAO.updateCheckOut("B001");

        System.out.println("\n=== FULL FLOW TEST COMPLETE ✅ ===");
    }
}