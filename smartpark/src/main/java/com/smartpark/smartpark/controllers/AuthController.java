package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.User;
import com.smartpark.smartpark.services.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthService authService = new AuthService();

    // POST /api/auth/register
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String role = request.getOrDefault("role", "customer");

        boolean success = authService.register(username, email, password, role);
        if (success) {
            response.put("status", "success");
            response.put("message", "User registered successfully!");
        } else {
            response.put("status", "error");
            response.put("message", "User already exists!");
        }
        return response;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");
        String password = request.get("password");

        User user = authService.login(email, password);
        if (user != null) {
            response.put("status", "success");
            response.put("message", "Login successful!");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("userId", user.getId());
        } else {
            response.put("status", "error");
            response.put("message", "Invalid email or password!");
        }
        return response;
    }
}