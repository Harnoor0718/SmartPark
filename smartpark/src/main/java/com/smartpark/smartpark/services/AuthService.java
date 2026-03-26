package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.UserDAO;
import com.smartpark.smartpark.models.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    // Register a new user
    public boolean register(String username, String email, String password, String role) {
        // Check if user already exists
        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            System.out.println("User already exists with email: " + email);
            return false;
        }
        // Create new user and save
        User newUser = new User(username, email, password, role);
        return userDAO.save(newUser);
    }

    // Login user
    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            System.out.println("No user found with email: " + email);
            return null;
        }
        if (!user.getPassword().equals(password)) {
            System.out.println("Incorrect password!");
            return null;
        }
        System.out.println("Login successful! Welcome " + user.getUsername());
        return user;
    }
}