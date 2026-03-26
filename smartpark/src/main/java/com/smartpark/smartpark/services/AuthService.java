package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.UserDAO;
import com.smartpark.smartpark.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthService {

    private UserDAO userDAO = new UserDAO();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register a new user
    public boolean register(String username, String email, String password, String role) {
        // Check if user already exists
        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            System.out.println("User already exists with email: " + email);
            return false;
        }
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, email, hashedPassword, role);
        return userDAO.save(newUser);
    }

    // Login user
    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            System.out.println("No user found with email: " + email);
            return null;
        }
        // Compare raw password with hashed password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Incorrect password!");
            return null;
        }
        System.out.println("Login successful! Welcome " + user.getUsername());
        return user;
    }
}