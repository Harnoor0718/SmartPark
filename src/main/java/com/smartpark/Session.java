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
    private int bookingId;
    private String vehiclePlate;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public void setSelectedSlot(String number, String type) {
        this.selectedSlotNumber = number;
        this.selectedSlotType = type;
    }

    public void setSelectedSlot(String number, String type, int id) {
        this.selectedSlotNumber = number;
        this.selectedSlotType = type;
        this.selectedSlotId = id;
    }

    public String getSelectedSlotNumber() { return selectedSlotNumber; }
    public String getSelectedSlotType() { return selectedSlotType; }
    public int getSelectedSlotId() { return selectedSlotId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }

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
}