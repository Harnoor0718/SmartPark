package com.smartpark.models;

import java.time.LocalDateTime;

public class VehicleHistory {

    private int id;
    private int vehicleId;
    private int bookingId;
    private String slotNumber;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private double totalAmount;

    public VehicleHistory(int vehicleId, int bookingId, String slotNumber,
                          LocalDateTime checkIn, LocalDateTime checkOut, double totalAmount) {
        this.vehicleId = vehicleId;
        this.bookingId = bookingId;
        this.slotNumber = slotNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
    }

    public int getId() { return id; }
    public int getVehicleId() { return vehicleId; }
    public int getBookingId() { return bookingId; }
    public String getSlotNumber() { return slotNumber; }
    public LocalDateTime getCheckIn() { return checkIn; }
    public LocalDateTime getCheckOut() { return checkOut; }
    public double getTotalAmount() { return totalAmount; }

    public void setId(int id) { this.id = id; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
