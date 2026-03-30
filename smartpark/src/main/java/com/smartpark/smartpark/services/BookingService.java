package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.BookingDAO;
import com.smartpark.smartpark.dao.SlotDAO;
import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.models.ParkingSlot;

import java.util.List;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();
    private SlotDAO slotDAO = new SlotDAO();

    // Create a new booking
    public Booking createBooking(int userId, int slotId, String vehiclePlate) {

        // Step 1 - Check if slot is available
        ParkingSlot slot = slotDAO.findById(slotId);
        if (slot == null) {
            System.out.println("Slot not found: " + slotId);
            return null;
        }
        if (slot.isOccupied()) {
            System.out.println("Slot already occupied: " + slotId);
            return null;
        }

        // Step 2 - Create booking object
        Booking booking = new Booking(userId, slotId, vehiclePlate);
        booking.setStatus(Booking.BookingStatus.PENDING);

        // Step 3 - Save booking to DB
        int bookingId = bookingDAO.save(booking);
        if (bookingId == -1) {
            System.out.println("Failed to save booking");
            return null;
        }
        booking.setId(bookingId);

        // Step 4 - Mark slot as occupied
        slotDAO.updateOccupied(slotId, true);

        System.out.println("Booking created successfully! ID: " + bookingId);
        return booking;
    }

    // Get booking by ID
    public Booking getBooking(int bookingId) {
        return bookingDAO.findById(bookingId);
    }

    // Get all bookings for a user
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.findByUserId(userId);
    }

    // Cancel a booking
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) return false;

        // Free up the slot
        slotDAO.updateOccupied(booking.getSlotId(), false);

        // Update status to cancelled
        return bookingDAO.updateStatus(bookingId, Booking.BookingStatus.CANCELLED);
    }
}