package com.smartpark.smartpark.interfaces;

public interface Payable {
    double calculateBill(double hoursParked);
    void printReceipt();
}