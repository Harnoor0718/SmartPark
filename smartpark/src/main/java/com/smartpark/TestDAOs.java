package com.smartpark;

import com.smartpark.smartpark.dao.*;
import com.smartpark.smartpark.models.*;

public class TestDAOs {

    public static void main(String[] args) {
        // Debug: generate a fresh hash and verify it
        // String fresh = org.mindrot.jbcrypt.BCrypt.hashpw("pass123", org.mindrot.jbcrypt.BCrypt.gensalt());
        // System.out.println("Fresh hash: " + fresh);
        // System.out.println("Verify test: " + org.mindrot.jbcrypt.BCrypt.checkpw("pass123", fresh));

        System.out.println("========== TESTING ALL DAOs ==========");

        // 1. Test LocationDAO
        System.out.println("\n--- LocationDAO ---");
        LocationDAO locationDAO = new LocationDAO();
        locationDAO.findAll().forEach(l ->
            System.out.println("Location: " + l.getName() + " | City: " + l.getCity() + " | Slots: " + l.getTotalSlots())
        );

        // 2. Test SlotDAO
        System.out.println("\n--- SlotDAO ---");
        SlotDAO slotDAO = new SlotDAO();
        slotDAO.findAll().forEach(s ->
            System.out.println("Slot: " + s.getSlotNumber() + " | Type: " + s.getSlotType() + " | Occupied: " + s.isOccupied())
        );

        // 3. Test SlotDAO findAvailable
        System.out.println("\n--- Available CAR Slots ---");
        slotDAO.findAvailable("CAR").forEach(s ->
            System.out.println("Available: " + s.getSlotNumber())
        );

        // 4. Test UserDAO
        System.out.println("\n--- UserDAO ---");
        UserDAO userDAO = new UserDAO();
        userDAO.findAll().forEach(u ->
            System.out.println("User: " + u.getUsername() + " | Email: " + u.getEmail() + " | Role: " + u.getRole())
        );

        // 5. Test verifyPassword
        System.out.println("\n--- Password Verification ---");
        User user = userDAO.findByEmail("admin@smartpark.com");
        if (user != null) {
            boolean valid = userDAO.verifyPassword("pass123", user.getPassword());
            System.out.println("Password check for admin: " + (valid ? "PASSED ✓" : "FAILED ✗"));
        }

        System.out.println("\n========== ALL TESTS DONE ==========");
    }
}