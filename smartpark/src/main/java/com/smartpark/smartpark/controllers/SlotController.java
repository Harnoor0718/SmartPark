package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.ParkingSlot;
import com.smartpark.smartpark.services.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "*")
public class SlotController {

    private ParkingService parkingService = new ParkingService();

    // GET /api/slots - get all slots
    @GetMapping
    public Map<String, Object> getAllSlots() {
        Map<String, Object> response = new HashMap<>();
        List<ParkingSlot> slots = parkingService.getAllSlots();
        response.put("status", "success");
        response.put("total", slots.size());
        response.put("slots", slots);
        return response;
    }

    // GET /api/slots/available - get only available slots
    @GetMapping("/available")
    public Map<String, Object> getAvailableSlots() {
        Map<String, Object> response = new HashMap<>();
        List<ParkingSlot> slots = parkingService.getAvailableSlots();
        response.put("status", "success");
        response.put("available", slots.size());
        response.put("slots", slots);
        return response;
    }

    // GET /api/slots/{id}/check - check if specific slot is available
    @GetMapping("/{id}/check")
    public Map<String, Object> checkSlot(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        boolean available = parkingService.isSlotAvailable(id);
        response.put("status", "success");
        response.put("slotId", id);
        response.put("available", available);
        return response;
    }
}