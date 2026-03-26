package com.smartpark.smartpark.models;

import java.time.LocalDateTime;

public class Booking {

    public enum BookingStatus {
        PENDING, ACTIVE, COMPLETED, CANCELLED
    }

    private int id;
    private int userId;
    private int slotId;
    private String vehiclePlate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double totalAmount;
    private BookingStatus status;

    public Booking(int userId, int slotId, String vehiclePlate) {
        this.userId = userId;
        this.slotId = slotId;
        this.vehiclePlate = vehiclePlate;
        this.status = BookingStatus.PENDING;
    }

    public Booking() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Booking #" + id + " | Slot: " + slotId + " | Vehicle: " + vehiclePlate + " | Status: " + status;
    }
}