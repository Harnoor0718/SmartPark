package com.smartpark.smartpark.services;

import com.smartpark.smartpark.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RevenueService {

    // Get daily revenue
    public List<Map<String, Object>> getDailyRevenue() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT DATE(check_out_time) as date, SUM(total_amount) as revenue, COUNT(*) as bookings " +
                     "FROM bookings WHERE status = 'COMPLETED' AND check_out_time IS NOT NULL " +
                     "GROUP BY DATE(check_out_time) ORDER BY date DESC LIMIT 7";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("date", rs.getString("date"));
                row.put("revenue", rs.getDouble("revenue"));
                row.put("bookings", rs.getInt("bookings"));
                result.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error getting daily revenue: " + e.getMessage());
        }
        return result;
    }

    // Get weekly revenue
    public List<Map<String, Object>> getWeeklyRevenue() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT WEEK(check_out_time) as week, YEAR(check_out_time) as year, " +
                     "SUM(total_amount) as revenue, COUNT(*) as bookings " +
                     "FROM bookings WHERE status = 'COMPLETED' AND check_out_time IS NOT NULL " +
                     "GROUP BY YEAR(check_out_time), WEEK(check_out_time) ORDER BY year DESC, week DESC LIMIT 4";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("week", rs.getInt("week"));
                row.put("year", rs.getInt("year"));
                row.put("revenue", rs.getDouble("revenue"));
                row.put("bookings", rs.getInt("bookings"));
                result.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error getting weekly revenue: " + e.getMessage());
        }
        return result;
    }

    // Get monthly revenue
    public List<Map<String, Object>> getMonthlyRevenue() {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT MONTH(check_out_time) as month, YEAR(check_out_time) as year, " +
                     "SUM(total_amount) as revenue, COUNT(*) as bookings " +
                     "FROM bookings WHERE status = 'COMPLETED' AND check_out_time IS NOT NULL " +
                     "GROUP BY YEAR(check_out_time), MONTH(check_out_time) ORDER BY year DESC, month DESC LIMIT 6";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("month", rs.getInt("month"));
                row.put("year", rs.getInt("year"));
                row.put("revenue", rs.getDouble("revenue"));
                row.put("bookings", rs.getInt("bookings"));
                result.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error getting monthly revenue: " + e.getMessage());
        }
        return result;
    }

    // Get total revenue summary
    public Map<String, Object> getRevenueSummary() {
        Map<String, Object> summary = new HashMap<>();
        String sql = "SELECT SUM(total_amount) as total, COUNT(*) as bookings FROM bookings WHERE status = 'COMPLETED'";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                summary.put("totalRevenue", rs.getDouble("total"));
                summary.put("totalBookings", rs.getInt("bookings"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting revenue summary: " + e.getMessage());
        }
        return summary;
    }
    // Get peak hours
public List<Map<String, Object>> getPeakHours() {
    List<Map<String, Object>> result = new ArrayList<>();
    String sql = "SELECT HOUR(check_in_time) as hour, COUNT(*) as bookings " +
                 "FROM bookings WHERE check_in_time IS NOT NULL " +
                 "GROUP BY HOUR(check_in_time) ORDER BY hour ASC";
    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            int hour = rs.getInt("hour");
            row.put("hour", String.format("%02d:00", hour));
            row.put("bookings", rs.getInt("bookings"));
            result.add(row);
        }
    } catch (SQLException e) {
        System.out.println("Error getting peak hours: " + e.getMessage());
    }
    return result;
}
}