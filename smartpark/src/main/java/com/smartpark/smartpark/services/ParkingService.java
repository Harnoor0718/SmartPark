package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.SlotDAO;
import com.smartpark.smartpark.models.ParkingSlot;

import java.util.List;

public class ParkingService {

    private SlotDAO slotDAO = new SlotDAO();

    // Get all slots
    public List<ParkingSlot> getAllSlots() {
        return slotDAO.findAll();
    }

    // Get only available slots
    public List<ParkingSlot> getAvailableSlots() {
        return slotDAO.findAvailable();
    }

    // Check if a specific slot is available
    public boolean isSlotAvailable(int slotId) {
        ParkingSlot slot = slotDAO.findById(slotId);
        if (slot == null) return false;
        return !slot.isOccupied();
    }
}