package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.BookingDAO;
import com.smartpark.smartpark.dao.SlotDAO;
import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.models.ParkingSlot;

import java.sql.*;
import java.time.Duration;
import com.smartpark.smartpark.utils.DBConnection;

public class CheckOutService {

    private BookingDAO bookingDAO = new BookingDAO();
    private SlotDAO slotDAO = new SlotDAO();

    public Booking checkOut(int bookingId) {

        // Step 1 - Find the booking
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return null;
        }

        // Step 2 - Check if checked in
        if (booking.getStatus() != Booking.BookingStatus.ACTIVE) {
            System.out.println("Booking is not active!");
            return null;
        }

        // Step 3 - Calculate hours parked
        String sql = "SELECT check_in_time FROM bookings WHERE id = ?";
        double hoursParked = 1.0; // default minimum 1 hour
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp checkIn = rs.getTimestamp("check_in_time");
                if (checkIn != null) {
                    long minutes = Duration.between(
                        checkIn.toLocalDateTime(),
                        java.time.LocalDateTime.now()
                    ).toMinutes();
                    hoursParked = Math.max(1.0, Math.ceil(minutes / 60.0));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating hours: " + e.getMessage());
        }

        // Step 4 - Get parking rate from slot type
        ParkingSlot slot = slotDAO.findById(booking.getSlotId());
        double ratePerHour = 50.0; // default CAR rate
        if (slot != null) {
            switch (slot.getSlotType()) {
                case BIKE: ratePerHour = 20.0; break;
                case TRUCK: ratePerHour = 100.0; break;
                default: ratePerHour = 50.0;
            }
        }

        // Step 5 - Calculate total amount
        double totalAmount = hoursParked * ratePerHour;

        // Step 6 - Update booking with checkout time and amount
        bookingDAO.updateCheckOut(bookingId, totalAmount);

        // Step 7 - Free the slot
        slotDAO.updateOccupied(booking.getSlotId(), false);

        System.out.println("Checked out! Hours: " + hoursParked + " | Amount: Rs." + totalAmount);

        // Step 8 - Return updated booking
        return bookingDAO.findById(bookingId);
    }
}