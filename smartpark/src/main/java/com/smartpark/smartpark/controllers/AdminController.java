package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.utils.DBConnection;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    // GET /api/admin/stats
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            Connection conn = DBConnection.getConnection();

            // Total users
            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs1 = ps1.executeQuery();
            int totalUsers = rs1.next() ? rs1.getInt(1) : 0;

            // Total bookings
            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM bookings");
            ResultSet rs2 = ps2.executeQuery();
            int totalBookings = rs2.next() ? rs2.getInt(1) : 0;

            // Occupied slots
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM parking_slots WHERE is_occupied = true");
            ResultSet rs3 = ps3.executeQuery();
            int occupiedSlots = rs3.next() ? rs3.getInt(1) : 0;

            // Total slots
            PreparedStatement ps4 = conn.prepareStatement("SELECT COUNT(*) FROM parking_slots");
            ResultSet rs4 = ps4.executeQuery();
            int totalSlots = rs4.next() ? rs4.getInt(1) : 0;

            // Today's revenue
            PreparedStatement ps5 = conn.prepareStatement(
                "SELECT COALESCE(SUM(total_amount), 0) FROM bookings WHERE status = 'COMPLETED' AND DATE(check_out_time) = CURDATE()");
            ResultSet rs5 = ps5.executeQuery();
            double todayRevenue = rs5.next() ? rs5.getDouble(1) : 0;

            // Total revenue
            PreparedStatement ps6 = conn.prepareStatement(
                "SELECT COALESCE(SUM(total_amount), 0) FROM bookings WHERE status = 'COMPLETED'");
            ResultSet rs6 = ps6.executeQuery();
            double totalRevenue = rs6.next() ? rs6.getDouble(1) : 0;

            response.put("status", "success");
            response.put("totalUsers", totalUsers);
            response.put("totalBookings", totalBookings);
            response.put("occupiedSlots", occupiedSlots);
            response.put("totalSlots", totalSlots);
            response.put("availableSlots", totalSlots - occupiedSlots);
            response.put("occupancyRate", totalSlots > 0 ? (occupiedSlots * 100 / totalSlots) + "%" : "0%");
            response.put("todayRevenue", todayRevenue);
            response.put("totalRevenue", totalRevenue);

        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // GET /api/admin/users
    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> users = new ArrayList<>();
        String sql = "SELECT id, username, email, role, created_at FROM users";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                user.put("role", rs.getString("role"));
                user.put("createdAt", rs.getString("created_at"));
                users.add(user);
            }
            response.put("status", "success");
            response.put("total", users.size());
            response.put("users", users);
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // GET /api/admin/slots
    @GetMapping("/slots")
    public Map<String, Object> getAllSlotsAdmin() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> slots = new ArrayList<>();
        String sql = "SELECT ps.*, l.name as location_name FROM parking_slots ps JOIN locations l ON ps.location_id = l.id";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> slot = new HashMap<>();
                slot.put("id", rs.getInt("id"));
                slot.put("slotNumber", rs.getString("slot_number"));
                slot.put("slotType", rs.getString("slot_type"));
                slot.put("isOccupied", rs.getBoolean("is_occupied"));
                slot.put("locationName", rs.getString("location_name"));
                slots.add(slot);
            }
            response.put("status", "success");
            response.put("total", slots.size());
            response.put("slots", slots);
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
}