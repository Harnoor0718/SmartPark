package com.smartpark.dao;

import com.smartpark.interfaces.Searchable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleHistoryDAO extends BaseDAO implements Searchable {

    // Save a vehicle history record
    public void save(String id, String vehicleId, String slotId,
                     int durationMins, double amount) {
        String sql = "INSERT INTO vehicle_history (id, vehicle_id, slot_id, check_in, duration_mins, amount, created_at) VALUES (?, ?, ?, NOW(), ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, vehicleId);
            stmt.setString(3, slotId);
            stmt.setInt(4, durationMins);
            stmt.setDouble(5, amount);
            stmt.executeUpdate();
            System.out.println("History saved: " + id);

        } catch (SQLException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    // Find history by vehicle ID
    public void findByVehicleId(String vehicleId) {
        String sql = "SELECT * FROM vehicle_history WHERE vehicle_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("History: " + rs.getString("id")
                        + " | Duration: " + rs.getInt("duration_mins") + " mins"
                        + " | Amount: " + rs.getDouble("amount"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding history: " + e.getMessage());
        }
    }

    // Search by license plate - implements Searchable
    @Override
    public List<?> searchByPlate(String licensePlate) {
        String sql = "SELECT vh.* FROM vehicle_history vh " +
                     "JOIN vehicles v ON vh.vehicle_id = v.vehicle_id " +
                     "WHERE v.license_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Found history for plate: " + licensePlate
                        + " | Duration: " + rs.getInt("duration_mins") + " mins");
            }

        } catch (SQLException e) {
            System.out.println("Error searching by plate: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // Filter by date range - implements Searchable
    @Override
    public List<?> filterByDateRange(String startDate, String endDate) {
        String sql = "SELECT * FROM vehicle_history WHERE check_in BETWEEN ? AND ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("History in range: " + rs.getString("id")
                        + " | Check in: " + rs.getString("check_in"));
            }

        } catch (SQLException e) {
            System.out.println("Error filtering by date: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}