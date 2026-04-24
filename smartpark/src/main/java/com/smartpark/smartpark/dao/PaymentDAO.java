package com.smartpark.smartpark.dao;

import com.smartpark.smartpark.utils.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentDAO extends BaseDAO {

    public int save(int bookingId, double amount, String paymentMethod, double hoursParked) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_method, payment_status, payment_time, hours_parked) " +
                     "VALUES (?, ?, ?, 'COMPLETED', NOW(), ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, paymentMethod);
            ps.setDouble(4, hoursParked);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error saving payment: " + e.getMessage());
        }
        return -1;
    }

    public Map<String, Object> findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> payment = new HashMap<>();
                payment.put("id", rs.getInt("id"));
                payment.put("bookingId", rs.getInt("booking_id"));
                payment.put("amount", rs.getDouble("amount"));
                payment.put("paymentMethod", rs.getString("payment_method"));
                payment.put("paymentStatus", rs.getString("payment_status"));
                payment.put("paymentTime", rs.getString("payment_time"));
                payment.put("hoursParked", rs.getDouble("hours_parked"));
                return payment;
            }
        } catch (SQLException e) {
            System.out.println("Error finding payment: " + e.getMessage());
        }
        return null;
    }
}