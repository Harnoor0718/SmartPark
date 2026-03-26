package com.smartpark.smartpark.models;

public class ParkingSlot {

    public enum SlotType {
        CAR, BIKE, TRUCK
    }

    private int id;
    private String slotNumber;
    private SlotType slotType;
    private boolean isOccupied;
    private int locationId;

    public ParkingSlot(String slotNumber, SlotType slotType, int locationId) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.isOccupied = false;
        this.locationId = locationId;
    }

    public ParkingSlot() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }

    public SlotType getSlotType() { return slotType; }
    public void setSlotType(SlotType slotType) { this.slotType = slotType; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    @Override
    public String toString() {
        return "Slot " + slotNumber + " [" + slotType + "] - " + (isOccupied ? "Occupied" : "Available");
    }
}