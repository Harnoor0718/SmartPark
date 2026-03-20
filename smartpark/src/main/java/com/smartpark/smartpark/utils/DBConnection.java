package com.smartpark.smartpark.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/smartpark_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Harnoor@123";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}