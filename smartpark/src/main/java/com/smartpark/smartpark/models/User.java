package com.smartpark.smartpark.models;

/**
 * Represents a registered user in the SmartPark system.
 * Users can have roles of either "admin" or "customer".
 * Passwords are stored as BCrypt hashes.
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
public class User {

    /** Unique identifier for the user */
    private int id;

    /** Username chosen by the user */
    private String username;

    /** Email address used for login */
    private String email;

    /** BCrypt hashed password */
    private String password;

    /** Role of the user — either "admin" or "customer" */
    private String role;

    /**
     * Constructor to create a new User object.
     *
     * @param username the chosen username
     * @param email the user's email address
     * @param password the BCrypt hashed password
     * @param role the user's role (admin or customer)
     */
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /** Default constructor */
    public User() {}

    /**
     * Gets the user ID.
     * @return the user ID
     */
    public int getId() { return id; }

    /**
     * Sets the user ID.
     * @param id the user ID to set
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Sets the username.
     * @param username the username to set
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Gets the email address.
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Sets the email address.
     * @param email the email to set
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Gets the hashed password.
     * @return the BCrypt hashed password
     */
    public String getPassword() { return password; }

    /**
     * Sets the hashed password.
     * @param password the BCrypt hashed password to set
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Gets the user role.
     * @return the role (admin or customer)
     */
    public String getRole() { return role; }

    /**
     * Sets the user role.
     * @param role the role to set
     */
    public void setRole(String role) { this.role = role; }

    /**
     * Returns a string representation of the User object.
     * @return formatted string with user details
     */
    @Override
    public String toString() {
        return "User{id=" + id + ", username=" + username + ", email=" + email + ", role=" + role + "}";
    }
}