package com.smartpark.smartpark.interfaces;

public interface Reportable {
    String generateReport();
    double getTotalRevenue();
    int getTotalBookings();
    int getOccupiedSlots();
}