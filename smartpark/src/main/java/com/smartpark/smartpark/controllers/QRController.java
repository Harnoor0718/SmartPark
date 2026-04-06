package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.dao.BookingDAO;
import com.smartpark.smartpark.models.Booking;
import com.smartpark.smartpark.utils.QRCodeGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*")
public class QRController {

    private BookingDAO bookingDAO = new BookingDAO();

    // GET /api/booking/{id}/qr
    @GetMapping("/{id}/qr")
    public Map<String, Object> getQRCode(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();

        Booking booking = bookingDAO.findById(id);
        if (booking == null) {
            response.put("status", "error");
            response.put("message", "Booking not found!");
            return response;
        }

        // Build QR content
        String qrContent = "SmartPark Booking" +
                "\nBooking ID: " + booking.getId() +
                "\nVehicle: " + booking.getVehiclePlate() +
                "\nSlot: " + booking.getSlotId() +
                "\nStatus: " + booking.getStatus();

        // Generate QR code as Base64
        String qrBase64 = QRCodeGenerator.generateQRCodeBase64(qrContent, 300, 300);

        if (qrBase64 != null) {
            response.put("status", "success");
            response.put("bookingId", booking.getId());
            response.put("vehiclePlate", booking.getVehiclePlate());
            response.put("qrCode", qrBase64);
            response.put("message", "QR code generated successfully!");
        } else {
            response.put("status", "error");
            response.put("message", "Failed to generate QR code!");
        }
        return response;
    }
}