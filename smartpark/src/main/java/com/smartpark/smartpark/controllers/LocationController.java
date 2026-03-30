package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.dao.LocationDAO;
import com.smartpark.smartpark.models.Location;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    private LocationDAO locationDAO = new LocationDAO();

    // GET /api/locations
    @GetMapping
    public Map<String, Object> getAllLocations() {
        Map<String, Object> response = new HashMap<>();
        List<Location> locations = locationDAO.findAll();
        response.put("status", "success");
        response.put("total", locations.size());
        response.put("locations", locations);
        return response;
    }

    // GET /api/locations/{id}
    @GetMapping("/{id}")
    public Map<String, Object> getLocation(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        Location location = locationDAO.findById(id);
        if (location != null) {
            response.put("status", "success");
            response.put("location", location);
        } else {
            response.put("status", "error");
            response.put("message", "Location not found!");
        }
        return response;
    }
}