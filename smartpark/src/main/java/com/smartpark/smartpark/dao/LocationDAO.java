package com.smartpark.smartpark.dao;

import com.smartpark.smartpark.models.Location;
import com.smartpark.smartpark.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    public List<Location> findAll() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Location loc = new Location();
                loc.setId(rs.getInt("id"));
                loc.setName(rs.getString("name"));
                loc.setAddress(rs.getString("address"));
                loc.setCity(rs.getString("city"));
                loc.setTotalSlots(rs.getInt("total_slots"));
                loc.setAvailableSlots(rs.getInt("available_slots"));
                locations.add(loc);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching locations: " + e.getMessage());
        }
        return locations;
    }

    public Location findById(int id) {
        String sql = "SELECT * FROM locations WHERE id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Location loc = new Location();
                loc.setId(rs.getInt("id"));
                loc.setName(rs.getString("name"));
                loc.setAddress(rs.getString("address"));
                loc.setCity(rs.getString("city"));
                loc.setTotalSlots(rs.getInt("total_slots"));
                loc.setAvailableSlots(rs.getInt("available_slots"));
                return loc;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching location: " + e.getMessage());
        }
        return null;
    }
}