package com.smartpark.service;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyticsService {

    private Connection connection;

    public AnalyticsService(Connection connection) {
        this.connection = connection;
    }

    // Top 3 most parked vehicle types
    public Map<String, Integer> getTopVehicles() throws SQLException {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT vehicle_type, COUNT(*) as count FROM vehicle_history GROUP BY vehicle_type ORDER BY count DESC LIMIT 3";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            result.put(rs.getString("vehicle_type"), rs.getInt("count"));
        }
        return result;
    }

    // Peak parking hours
    public Map<Integer, Integer> getPeakHours() throws SQLException {
        Map<Integer, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT HOUR(check_in) as hour, COUNT(*) as count FROM vehicle_history GROUP BY hour ORDER BY count DESC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            result.put(rs.getInt("hour"), rs.getInt("count"));
        }
        return result;
    }

    // Total revenue
    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(total_amount) as revenue FROM vehicle_history";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            return rs.getDouble("revenue");
        }
        return 0.0;
    }

    // Occupancy rate (booked slots / total slots)
    public double getOccupancyRate(int totalSlots) throws SQLException {
        String sql = "SELECT COUNT(*) as active FROM vehicle_history WHERE check_out IS NULL";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int active = rs.getInt("active");
            return (double) active / totalSlots * 100;
        }
        return 0.0;
    }
}
