package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.services.CheckInService;
import com.smartpark.smartpark.services.CheckOutService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CheckInOutController {

    private CheckInService checkInService = new CheckInService();
    private CheckOutService checkOutService = new CheckOutService();

    // POST /api/checkin
    @PostMapping("/checkin")
    public Map<String, Object> checkIn(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int bookingId = (int) request.get("bookingId");
            Booking booking = checkInService.checkIn(bookingId);
            if (booking != null) {
                response.put("status", "success");
                response.put("message", "Check-in successful!");
                response.put("bookingId", booking.getId());
                response.put("bookingStatus", booking.getStatus());
                response.put("checkInTime", booking.getCheckInTime());
            } else {
                response.put("status", "error");
                response.put("message", "Check-in failed! Booking not found or already active.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Invalid request: " + e.getMessage());
        }
        return response;
    }

    // POST /api/checkout
    @PostMapping("/checkout")
    public Map<String, Object> checkOut(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int bookingId = (int) request.get("bookingId");
            Booking booking = checkOutService.checkOut(bookingId);
            if (booking != null) {
                response.put("status", "success");
                response.put("message", "Check-out successful!");
                response.put("bookingId", booking.getId());
                response.put("bookingStatus", booking.getStatus());
                response.put("totalAmount", booking.getTotalAmount());
                response.put("checkOutTime", booking.getCheckOutTime());
            } else {
                response.put("status", "error");
                response.put("message", "Check-out failed! Booking not active.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Invalid request: " + e.getMessage());
        }
        return response;
    }
}