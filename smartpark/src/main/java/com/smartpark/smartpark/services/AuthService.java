package com.smartpark.smartpark.services;

import com.smartpark.smartpark.dao.UserDAO;
import com.smartpark.smartpark.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Service class handling user authentication in SmartPark.
 * Provides registration and login functionality with BCrypt password hashing.
 * Demonstrates encapsulation — all auth logic is hidden inside this service.
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
public class AuthService {

    /** Data access object for user database operations */
    private UserDAO userDAO = new UserDAO();

    /** BCrypt password encoder for secure password hashing */
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Registers a new user in the SmartPark system.
     * Checks for duplicate email before saving.
     * Password is BCrypt hashed before storing.
     *
     * @param username the desired username
     * @param email the user's email address
     * @param password the plain text password (will be hashed)
     * @param role the user's role (admin or customer)
     * @return true if registration successful, false if email already exists
     */
    public boolean register(String username, String email, String password, String role) {
        User existingUser = userDAO.findByEmail(email);
        if (existingUser != null) {
            System.out.println("User already exists with email: " + email);
            return false;
        }
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(username, email, hashedPassword, role);
        return userDAO.save(newUser);
    }

    /**
     * Authenticates a user with email and password.
     * Uses BCrypt to compare plain text password with stored hash.
     *
     * @param email the user's email address
     * @param password the plain text password to verify
     * @return the authenticated User object, or null if authentication fails
     */
    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            System.out.println("No user found with email: " + email);
            return null;
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Incorrect password!");
            return null;
        }
        System.out.println("Login successful! Welcome " + user.getUsername());
        return user;
    }
}