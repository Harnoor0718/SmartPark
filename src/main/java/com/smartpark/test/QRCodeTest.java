package com.smartpark.test;

import com.smartpark.service.QRCodeGenerator;

public class QRCodeTest {

    public static void main(String[] args) {
        try {
            String bookingData = "BookingID:101|VehicleID:V001|Slot:A12";

            QRCodeGenerator.saveQR(bookingData, "SmartParkQR.png", 300, 300);

            System.out.println("SUCCESS! QR Code generated!");
            System.out.println("Check SmartParkQR.png in your project folder!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
