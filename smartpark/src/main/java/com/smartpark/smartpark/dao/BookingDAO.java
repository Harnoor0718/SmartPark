package com.smartpark.smartpark.dao;

import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // Save a new booking
    public int save(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, slot_id, vehicle_plate, status) VALUES (?, ?, ?, ?)";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getSlotId());
            ps.setString(3, booking.getVehiclePlate());
            ps.setString(4, booking.getStatus().name());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
        return -1;
    }

    // Find booking by ID
    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error finding booking: " + e.getMessage());
        }
        return null;
    }

    // Find all bookings by user
    public List<Booking> findByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) bookings.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error finding bookings: " + e.getMessage());
        }
        return bookings;
    }

    // Update booking status
    public boolean updateStatus(int bookingId, Booking.BookingStatus status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status.name());
            ps.setInt(2, bookingId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }

    // Update check-out time and total amount
    public boolean updateCheckOut(int bookingId, double totalAmount) {
        String sql = "UPDATE bookings SET check_out_time = NOW(), total_amount = ?, status = 'COMPLETED' WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, totalAmount);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating checkout: " + e.getMessage());
            return false;
        }
    }

    // Helper method to map ResultSet to Booking object
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setSlotId(rs.getInt("slot_id"));
        booking.setVehiclePlate(rs.getString("vehicle_plate"));
        booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
        booking.setTotalAmount(rs.getDouble("total_amount"));
        Timestamp checkIn = rs.getTimestamp("check_in_time");
        Timestamp checkOut = rs.getTimestamp("check_out_time");
        if (checkIn != null) booking.setCheckInTime(checkIn.toLocalDateTime());
        if (checkOut != null) booking.setCheckOutTime(checkOut.toLocalDateTime());
        return booking;
    }
}