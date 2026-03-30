package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.services.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private BookingService bookingService = new BookingService();

    // POST /api/bookings - create a new booking
    @PostMapping
    public Map<String, Object> createBooking(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int userId = (int) request.get("userId");
            int slotId = (int) request.get("slotId");
            String vehiclePlate = (String) request.get("vehiclePlate");

            Booking booking = bookingService.createBooking(userId, slotId, vehiclePlate);
            if (booking != null) {
                response.put("status", "success");
                response.put("message", "Booking created successfully!");
                response.put("bookingId", booking.getId());
                response.put("slotId", booking.getSlotId());
                response.put("vehiclePlate", booking.getVehiclePlate());
                response.put("bookingStatus", booking.getStatus());
            } else {
                response.put("status", "error");
                response.put("message", "Slot not available or booking failed!");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Invalid request: " + e.getMessage());
        }
        return response;
    }

    // GET /api/bookings/{id} - get booking by ID
    @GetMapping("/{id}")
    public Map<String, Object> getBooking(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        Booking booking = bookingService.getBooking(id);
        if (booking != null) {
            response.put("status", "success");
            response.put("booking", booking);
        } else {
            response.put("status", "error");
            response.put("message", "Booking not found!");
        }
        return response;
    }

    // GET /api/bookings/user/{userId} - get all bookings for a user
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserBookings(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();
        List<Booking> bookings = bookingService.getUserBookings(userId);
        response.put("status", "success");
        response.put("total", bookings.size());
        response.put("bookings", bookings);
        return response;
    }

    // DELETE /api/bookings/{id}/cancel - cancel a booking
    @DeleteMapping("/{id}/cancel")
    public Map<String, Object> cancelBooking(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        boolean cancelled = bookingService.cancelBooking(id);
        if (cancelled) {
            response.put("status", "success");
            response.put("message", "Booking cancelled successfully!");
        } else {
            response.put("status", "error");
            response.put("message", "Booking not found or already cancelled!");
        }
        return response;
    }
}