package com.smartpark.smartpark.dao;

import com.smartpark.smartpark.models.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO extends BaseDAO {

    public boolean save(Location location) {
        String sql = "INSERT INTO locations (name, address, city, total_slots, available_slots) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, location.getName());
            ps.setString(2, location.getAddress());
            ps.setString(3, location.getCity());
            ps.setInt(4, location.getTotalSlots());
            ps.setInt(5, location.getAvailableSlots());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error saving location: " + e.getMessage());
            return false;
        }
    }

    public Location findById(int id) {
        String sql = "SELECT * FROM locations WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Error finding location: " + e.getMessage());
        }
        return null;
    }

    public List<Location> findAll() {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM locations";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching locations: " + e.getMessage());
        }
        return list;
    }

    private Location mapRow(ResultSet rs) throws SQLException {
        Location loc = new Location();
        loc.setId(rs.getInt("id"));
        loc.setName(rs.getString("name"));
        loc.setAddress(rs.getString("address"));
        loc.setCity(rs.getString("city"));
        loc.setTotalSlots(rs.getInt("total_slots"));
        loc.setAvailableSlots(rs.getInt("available_slots"));
        return loc;
    }
}