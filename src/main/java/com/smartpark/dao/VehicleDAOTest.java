package com.smartpark.dao;

import com.smartpark.models.Car;
import com.smartpark.models.Bike;
import com.smartpark.models.Truck;

public class VehicleDAOTest {

    public static void main(String[] args) {

        VehicleDAO dao = new VehicleDAO();

        // Create 3 vehicle types
        Car car = new Car("V001", "DL1234", "U001");
        Bike bike = new Bike("V002", "DL5678", "U002");
        Truck truck = new Truck("V003", "DL9999", "U003");

        // Test save()
        System.out.println("=== Testing save() ===");
        dao.save(car);
        dao.save(bike);
        dao.save(truck);

        // Test findByUserId()
        System.out.println("\n=== Testing findByUserId() ===");
        dao.findByUserId("U001");

        // Test findByLicensePlate()
        System.out.println("\n=== Testing findByLicensePlate() ===");
        dao.findByLicensePlate("DL1234");

        System.out.println("\nVehicleDAO test complete! âœ…");
    }
}