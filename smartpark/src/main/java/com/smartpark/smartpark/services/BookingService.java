package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.BookingDAO;
import com.smartpark.smartpark.dao.SlotDAO;
import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.models.ParkingSlot;

import java.util.List;

/**
 * Service class handling all parking booking operations in SmartPark.
 * Manages the full booking lifecycle — creation, retrieval and cancellation.
 * Ensures slot availability before creating a booking and frees slots on cancellation.
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
public class BookingService {

    /** DAO for booking database operations */
    private BookingDAO bookingDAO = new BookingDAO();

    /** DAO for parking slot database operations */
    private SlotDAO slotDAO = new SlotDAO();

    /**
     * Creates a new parking booking if the requested slot is available.
     * Automatically marks the slot as occupied after successful booking.
     *
     * @param userId the ID of the user making the booking
     * @param slotId the ID of the parking slot to book
     * @param vehiclePlate the vehicle's license plate number
     * @return the created Booking object, or null if slot unavailable or booking failed
     */
    public Booking createBooking(int userId, int slotId, String vehiclePlate) {
        ParkingSlot slot = slotDAO.findById(slotId);
        if (slot == null) {
            System.out.println("Slot not found: " + slotId);
            return null;
        }
        if (slot.isOccupied()) {
            System.out.println("Slot already occupied: " + slotId);
            return null;
        }
        Booking booking = new Booking(userId, slotId, vehiclePlate);
        booking.setStatus(Booking.BookingStatus.PENDING);
        int bookingId = bookingDAO.save(booking);
        if (bookingId == -1) {
            System.out.println("Failed to save booking");
            return null;
        }
        booking.setId(bookingId);
        slotDAO.updateOccupied(slotId, true);
        System.out.println("Booking created successfully! ID: " + bookingId);
        return booking;
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param bookingId the ID of the booking to retrieve
     * @return the Booking object, or null if not found
     */
    public Booking getBooking(int bookingId) {
        return bookingDAO.findById(bookingId);
    }

    /**
     * Retrieves all bookings made by a specific user.
     *
     * @param userId the ID of the user
     * @return list of Booking objects for the user
     */
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.findByUserId(userId);
    }

    /**
     * Cancels an existing booking and frees the associated parking slot.
     *
     * @param bookingId the ID of the booking to cancel
     * @return true if cancellation successful, false if booking not found
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) return false;
        slotDAO.updateOccupied(booking.getSlotId(), false);
        return bookingDAO.updateStatus(bookingId, Booking.BookingStatus.CANCELLED);
    }
}