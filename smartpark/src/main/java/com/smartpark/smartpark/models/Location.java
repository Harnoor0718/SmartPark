package com.smartpark.smartpark.models;

public class Location {

    private int id;
    private String name;
    private String address;
    private String city;
    private int totalSlots;
    private int availableSlots;

    public Location(String name, String address, String city, int totalSlots) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.totalSlots = totalSlots;
        this.availableSlots = totalSlots;
    }

    public Location() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }

    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }

    @Override
    public String toString() {
        return name + " | " + address + ", " + city + " | Slots: " + availableSlots + "/" + totalSlots;
    }
}