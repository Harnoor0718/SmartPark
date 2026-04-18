package com.smartpark;

public class Session {

    private static Session instance;

    private String token;
    private String username;
    private String role;
    private int userId;

    private String selectedSlotNumber;
    private String selectedSlotType;
    private int selectedSlotId;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // ================= AUTH =================
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // ================= SLOT =================
    public void setSelectedSlot(String number, String type, int slotId) {
        this.selectedSlotNumber = number;
        this.selectedSlotType = type;
        this.selectedSlotId = slotId;
    }

    public String getSelectedSlotNumber() {
        return selectedSlotNumber;
    }

    public String getSelectedSlotType() {
        return selectedSlotType;
    }

    public int getSelectedSlotId() {
        return selectedSlotId;
    }

    // ================= CLEAR =================
    public void clear() {
        token = null;
        username = null;
        role = null;
        userId = 0;
        selectedSlotNumber = null;
        selectedSlotType = null;
        selectedSlotId = 0;
        bookingId = 0;
        vehiclePlate = null;
    }

    private int bookingId;
    private String vehiclePlate;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }
}