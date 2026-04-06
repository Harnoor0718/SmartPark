package com.smartpark.smartpark.models;

import com.smartpark.smartpark.interfaces.Reportable;
import com.smartpark.smartpark.utils.DBConnection;

import java.sql.*;

public class ParkingLot implements Reportable {

    private String name;
    private int totalSlots;

    public ParkingLot(String name, int totalSlots) {
        this.name = name;
        this.totalSlots = totalSlots;
    }

    @Override
    public String generateReport() {
        return "=== SmartPark Report ===" +
                "\nLot: " + name +
                "\nTotal Slots: " + totalSlots +
                "\nOccupied: " + getOccupiedSlots() +
                "\nTotal Bookings: " + getTotalBookings() +
                "\nTotal Revenue: Rs." + getTotalRevenue();
    }

    @Override
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM bookings WHERE status = 'COMPLETED'";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error getting revenue: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getTotalBookings() {
        String sql = "SELECT COUNT(*) FROM bookings";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getting bookings count: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int getOccupiedSlots() {
        String sql = "SELECT COUNT(*) FROM parking_slots WHERE is_occupied = true";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getting occupied slots: " + e.getMessage());
        }
        return 0;
    }

    public String getName() { return name; }
    public int getTotalSlots() { return totalSlots; }
}