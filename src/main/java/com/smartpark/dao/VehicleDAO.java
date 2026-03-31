package com.smartpark.dao;

import com.smartpark.models.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends BaseDAO {

    // Save a vehicle to the database
    public void save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (vehicle_id, license_plate, vehicle_type, owner_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getVehicleId());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getVehicleType());
            stmt.setString(4, vehicle.getOwnerId());
            stmt.executeUpdate();
            System.out.println("Vehicle saved: " + vehicle.getLicensePlate());

        } catch (SQLException e) {
            System.out.println("Error saving vehicle: " + e.getMessage());
        }
    }

    // Find all vehicles by user ID
    public List<Vehicle> findByUserId(String userId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE owner_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Found: " + rs.getString("license_plate")
                        + " | Type: " + rs.getString("vehicle_type"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding vehicles: " + e.getMessage());
        }
        return vehicles;
    }

    // Find vehicle by license plate
    public Vehicle findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM vehicles WHERE license_plate = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, licensePlate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Found vehicle: " + rs.getString("license_plate")
                        + " | Type: " + rs.getString("vehicle_type"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding vehicle: " + e.getMessage());
        }
        return null;
    }

    // Find all vehicles
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Vehicle: " + rs.getString("license_plate")
                        + " | Type: " + rs.getString("vehicle_type"));
            }

        } catch (SQLException e) {
            System.out.println("Error finding all vehicles: " + e.getMessage());
        }
        return vehicles;
    }
}