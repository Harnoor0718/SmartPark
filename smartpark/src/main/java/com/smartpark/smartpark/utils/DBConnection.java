package com.smartpark.smartpark.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class providing a singleton JDBC database connection for SmartPark.
 * Implements the Singleton pattern — only one connection is maintained at a time.
 * Automatically reconnects if the connection is closed or invalid.
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
public class DBConnection {

    /** JDBC URL for the SmartPark MySQL database */
    private static final String URL = "jdbc:mysql://localhost:3306/smartpark_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    /** MySQL username */
    private static final String USERNAME = "root";

    /** MySQL password */
    private static final String PASSWORD = "Harnoor@123";

    /** Singleton connection instance */
    private static Connection connection = null;

    /**
     * Returns the active database connection.
     * Creates a new connection if none exists or if the current one is closed/invalid.
     * Uses DriverManager to establish JDBC connection to MySQL.
     *
     * @return active Connection object, or null if connection fails
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}