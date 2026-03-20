package com.smartpark.interfaces;

public interface Payable {

    double calculateBill(double hours);
    void printReceipt(double hours);
}