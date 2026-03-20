package com.smartpark;

public interface Payable {

    double calculateBill(double hours);
    void printReceipt(double hours);
}