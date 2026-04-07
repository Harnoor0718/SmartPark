package com.smartpark.smartpark.controllers;

import com.smartpark.smartpark.models.User;
import com.smartpark.smartpark.services.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller handling user authentication endpoints.
 * Provides register and login APIs with input validation.
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    /** Service handling authentication business logic */
    private AuthService authService = new AuthService();

    /**
     * Registers a new user in the SmartPark system.
     * Validates that all required fields are present and non-empty.
     *
     * @param request JSON request body containing username, email, password, role
     * @return JSON response with status and message
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        // Input validation
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");
        String role = request.getOrDefault("role", "customer");

        if (username == null || username.trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Username is required!");
            return response;
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            response.put("status", "error");
            response.put("message", "Valid email is required!");
            return response;
        }
        if (password == null || password.trim().length() < 4) {
            response.put("status", "error");
            response.put("message", "Password must be at least 4 characters!");
            return response;
        }

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

    /**
     * Authenticates a user with email and password.
     * Validates that email and password fields are present.
     *
     * @param request JSON request body containing email and password
     * @return JSON response with status, message and user details on success
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        // Input validation
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || email.trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Email is required!");
            return response;
        }
        if (password == null || password.trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Password is required!");
            return response;
        }

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