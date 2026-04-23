package com.smartpark.smartpark.dao;
import com.smartpark.smartpark.models.ParkingSlot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO extends BaseDAO {

    public boolean save(ParkingSlot slot) {
        String sql = "INSERT INTO parking_slots (slot_number, slot_type, is_occupied, location_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slot.getSlotNumber());
            ps.setString(2, slot.getSlotType().name());
            ps.setBoolean(3, slot.isOccupied());
            ps.setInt(4, slot.getLocationId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error saving slot: " + e.getMessage());
            return false;
        }
    }

    // ADD THIS METHOD
    public ParkingSlot findById(int id) {
        String sql = "SELECT * FROM parking_slots WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Error finding slot by id: " + e.getMessage());
        }
        return null;
    }

    public List<ParkingSlot> findAll() {
        List<ParkingSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_slots";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching slots: " + e.getMessage());
        }
        return list;
    }

    public List<ParkingSlot> findAvailable(String slotType) {
        List<ParkingSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_slots WHERE is_occupied = FALSE AND slot_type = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slotType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching available slots: " + e.getMessage());
        }
        return list;
    }

    public boolean updateOccupied(int slotId, boolean isOccupied) {
        String sql = "UPDATE parking_slots SET is_occupied = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isOccupied);
            ps.setInt(2, slotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating slot: " + e.getMessage());
            return false;
        }
    }

    // ONLY ONE mapRow — duplicate removed
    private ParkingSlot mapRow(ResultSet rs) throws SQLException {
        ParkingSlot slot = new ParkingSlot();
        slot.setId(rs.getInt("id"));
        slot.setSlotNumber(rs.getString("slot_number"));
        slot.setSlotType(ParkingSlot.SlotType.valueOf(rs.getString("slot_type")));
        slot.setOccupied(rs.getBoolean("is_occupied"));
        slot.setLocationId(rs.getInt("location_id"));
        return slot;
    }
}