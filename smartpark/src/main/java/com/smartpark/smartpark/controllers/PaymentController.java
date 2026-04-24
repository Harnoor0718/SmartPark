package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.dao.PaymentDAO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private PaymentDAO paymentDAO = new PaymentDAO();

    @PostMapping
    public Map<String, Object> createPayment(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int bookingId = ((Number) request.get("bookingId")).intValue();
            double amount = ((Number) request.get("amount")).doubleValue();
            String paymentMethod = (String) request.get("paymentMethod");
            double hoursParked = ((Number) request.get("hoursParked")).doubleValue();

            if (paymentMethod == null || paymentMethod.isEmpty()) {
                response.put("status", "error");
                response.put("message", "Payment method is required!");
                return response;
            }

            int paymentId = paymentDAO.save(bookingId, amount, paymentMethod, hoursParked);
            if (paymentId != -1) {
                response.put("status", "success");
                response.put("message", "Payment successful!");
                response.put("paymentId", paymentId);
                response.put("amount", amount);
                response.put("paymentMethod", paymentMethod);
                response.put("hoursParked", hoursParked);
            } else {
                response.put("status", "error");
                response.put("message", "Payment failed!");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Invalid request: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/{bookingId}")
    public Map<String, Object> getPayment(@PathVariable int bookingId) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> payment = paymentDAO.findByBookingId(bookingId);
        if (payment != null) {
            response.put("status", "success");
            response.putAll(payment);
        } else {
            response.put("status", "error");
            response.put("message", "Payment not found!");
        }
        return response;
    }
}