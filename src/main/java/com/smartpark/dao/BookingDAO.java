package com.smartpark.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends BaseDAO {

    // Save a booking + update slot in ONE transaction
    public void save(String bookingId, String vehicleId, String slotId, String userId) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // START TRANSACTION

            // Step 1 - Insert booking
            String bookingSql = "INSERT INTO bookings (booking_id, vehicle_id, slot_id, user_id, status, check_in_time) VALUES (?, ?, ?, ?, 'ACTIVE', NOW())";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingSql);
            bookingStmt.setString(1, bookingId);
            bookingStmt.setString(2, vehicleId);
            bookingStmt.setString(3, slotId);
            bookingStmt.setString(4, userId);
            bookingStmt.executeUpdate();

            // Step 2 - Update slot to occupied
            String slotSql = "UPDATE parking_slots SET is_occupied = true WHERE slot_id = ?";
            PreparedStatement slotStmt = conn.prepareStatement(slotSql);
            slotStmt.setString(1, slotId);
            slotStmt.executeUpdate();

            conn.commit(); // COMMIT TRANSACTION
            System.out.println("Booking saved and slot updated: " + bookingId);

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // ROLLBACK on failure
                System.out.println("Transaction failed, rolled back: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }
        } finally {
            close(conn);
        }
    }

    // Find booking by ID
    public void findById(String bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Booking found: " + rs.getString("booking_id")
                        + " | Status: " + rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding booking: " + e.getMessage());
        }
    }

    // Find bookings by vehicle
    public void findByVehicle(String vehicleId) {
        String sql = "SELECT * FROM bookings WHERE vehicle_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Booking: " + rs.getString("booking_id")
                        + " | Status: " + rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding bookings: " + e.getMessage());
        }
    }

    // Update booking status
    public void updateStatus(String bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, bookingId);
            stmt.executeUpdate();
            System.out.println("Booking status updated: " + bookingId + " -> " + status);

        } catch (SQLException e) {
            System.out.println("Error updating status: " + e.getMessage());
        }
    }

    // Update checkout time
    public void updateCheckOut(String bookingId) {
        String sql = "UPDATE bookings SET check_out_time = NOW(), status = 'COMPLETED' WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bookingId);
            stmt.executeUpdate();
            System.out.println("Checkout updated for booking: " + bookingId);

        } catch (SQLException e) {
            System.out.println("Error updating checkout: " + e.getMessage());
        }
    }
}