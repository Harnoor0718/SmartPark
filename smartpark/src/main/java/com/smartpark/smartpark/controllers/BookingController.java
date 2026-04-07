package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.services.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller handling parking booking endpoints.
 * Provides APIs for creating, retrieving and cancelling bookings.
 * Includes input validation on all endpoints.
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    /** Service handling booking business logic */
    private BookingService bookingService = new BookingService();

    /**
     * Creates a new parking booking.
     * Validates userId, slotId and vehiclePlate before processing.
     *
     * @param request JSON body with userId, slotId, vehiclePlate
     * @return JSON response with booking details on success
     */
    @PostMapping
    public Map<String, Object> createBooking(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Input validation
            if (request.get("userId") == null || request.get("slotId") == null) {
                response.put("status", "error");
                response.put("message", "userId and slotId are required!");
                return response;
            }
            String vehiclePlate = (String) request.get("vehiclePlate");
            if (vehiclePlate == null || vehiclePlate.trim().isEmpty()) {
                response.put("status", "error");
                response.put("message", "Vehicle plate is required!");
                return response;
            }

            int userId = (int) request.get("userId");
            int slotId = (int) request.get("slotId");

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

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the booking ID
     * @return JSON response with booking details
     */
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

    /**
     * Retrieves all bookings for a specific user.
     *
     * @param userId the user ID
     * @return JSON response with list of bookings
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserBookings(@PathVariable int userId) {
        Map<String, Object> response = new HashMap<>();
        List<Booking> bookings = bookingService.getUserBookings(userId);
        response.put("status", "success");
        response.put("total", bookings.size());
        response.put("bookings", bookings);
        return response;
    }

    /**
     * Cancels an existing booking.
     *
     * @param id the booking ID to cancel
     * @return JSON response with cancellation status
     */
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