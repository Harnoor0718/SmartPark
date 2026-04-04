package com.smartpark.dao;

import java.sql.*;

public class PaymentDAO extends BaseDAO {

    // Save a payment linked to a booking
    public void save(String paymentId, String bookingId, double amount, String status) {
        String sql = "INSERT INTO payments (payment_id, booking_id, amount, status, payment_time) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paymentId);
            stmt.setString(2, bookingId);
            stmt.setDouble(3, amount);
            stmt.setString(4, status);
            stmt.executeUpdate();
            System.out.println("Payment saved: " + paymentId + " for booking: " + bookingId);

        } catch (SQLException e) {
            System.out.println("Error saving payment: " + e.getMessage());
        }
    }

    // Find payment by booking ID
    public void findByBookingId(String bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Payment: " + rs.getString("payment_id")
                        + " | Amount: " + rs.getDouble("amount")
                        + " | Status: " + rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding payment: " + e.getMessage());
        }
    }

    // Update payment status
    public void updateStatus(String paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, paymentId);
            stmt.executeUpdate();
            System.out.println("Payment status updated: " + paymentId + " -> " + status);

        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
        }
    }
}