package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.BookingDAO;
import com.smartpark.smartpark.models.Booking;

import java.sql.*;
import com.smartpark.smartpark.utils.DBConnection;

public class CheckInService {

    private BookingDAO bookingDAO = new BookingDAO();

    public Booking checkIn(int bookingId) {

        // Step 1 - Find the booking
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return null;
        }

        // Step 2 - Check if already checked in
        if (booking.getStatus() == Booking.BookingStatus.ACTIVE) {
            System.out.println("Already checked in!");
            return null;
        }

        // Step 3 - Set check-in time and update status to ACTIVE
        String sql = "UPDATE bookings SET check_in_time = NOW(), status = 'ACTIVE' WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error during check-in: " + e.getMessage());
            return null;
        }

        // Step 4 - Return updated booking
        return bookingDAO.findById(bookingId);
    }
}