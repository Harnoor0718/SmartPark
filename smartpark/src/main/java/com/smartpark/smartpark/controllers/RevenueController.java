package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.ParkingLot;
import com.smartpark.smartpark.services.RevenueService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class RevenueController {

    private RevenueService revenueService = new RevenueService();

    // GET /api/reports/revenue?period=daily|weekly|monthly
    @GetMapping("/revenue")
    public Map<String, Object> getRevenue(@RequestParam String period) {
        Map<String, Object> response = new HashMap<>();
        switch (period.toLowerCase()) {
            case "daily":
                response.put("status", "success");
                response.put("period", "daily");
                response.put("data", revenueService.getDailyRevenue());
                break;
            case "weekly":
                response.put("status", "success");
                response.put("period", "weekly");
                response.put("data", revenueService.getWeeklyRevenue());
                break;
            case "monthly":
                response.put("status", "success");
                response.put("period", "monthly");
                response.put("data", revenueService.getMonthlyRevenue());
                break;
            default:
                response.put("status", "error");
                response.put("message", "Invalid period! Use daily, weekly or monthly");
        }
        return response;
    }

    // GET /api/reports/summary
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("summary", revenueService.getRevenueSummary());
        return response;
    }

    // GET /api/reports/lot
    @GetMapping("/lot")
    public Map<String, Object> getLotReport() {
        Map<String, Object> response = new HashMap<>();
        ParkingLot lot = new ParkingLot("SmartPark Main", 15);
        response.put("status", "success");
        response.put("report", lot.generateReport());
        response.put("totalRevenue", lot.getTotalRevenue());
        response.put("totalBookings", lot.getTotalBookings());
        response.put("occupiedSlots", lot.getOccupiedSlots());
        return response;
    }
}