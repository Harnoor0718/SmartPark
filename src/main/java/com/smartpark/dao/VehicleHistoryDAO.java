package com.smartpark.dao;

import com.smartpark.models.VehicleHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleHistoryDAO {

    private Connection connection;

    public VehicleHistoryDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(VehicleHistory vh) throws SQLException {
        String sql = "INSERT INTO vehicle_history (vehicle_id, booking_id, slot_number, check_in, check_out, total_amount) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, vh.getVehicleId());
        stmt.setInt(2, vh.getBookingId());
        stmt.setString(3, vh.getSlotNumber());
        stmt.setTimestamp(4, Timestamp.valueOf(vh.getCheckIn()));
        stmt.setTimestamp(5, Timestamp.valueOf(vh.getCheckOut()));
        stmt.setDouble(6, vh.getTotalAmount());
        stmt.executeUpdate();

        System.out.println("Vehicle history saved!");
    }

    public List<VehicleHistory> findByVehicleId(int vehicleId) throws SQLException {
        List<VehicleHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle_history WHERE vehicle_id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, vehicleId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            VehicleHistory vh = new VehicleHistory(
                rs.getInt("vehicle_id"),
                rs.getInt("booking_id"),
                rs.getString("slot_number"),
                rs.getTimestamp("check_in").toLocalDateTime(),
                rs.getTimestamp("check_out").toLocalDateTime(),
                rs.getDouble("total_amount")
            );
            vh.setId(rs.getInt("id"));
            list.add(vh);
        }
        return list;
    }
}
