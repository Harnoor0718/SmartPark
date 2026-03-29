package com.smartpark.dao;

import com.smartpark.models.Vehicle;
import java.util.ArrayList;
import java.util.List;



public class VehicleDAO {

    // Save a vehicle to the database
    public void save(Vehicle vehicle) {
        // TODO: Connect to DB (Manisha provides connection)
        System.out.println("Saving vehicle: " + vehicle.getVehicleType() +
                " | Plate: " + vehicle.getLicensePlate() +
                " | Owner: " + vehicle.getOwnerId());
    }

    // Find all vehicles by user ID
    public List<Vehicle> findByUserId(String userId) {
        // TODO: Connect to DB (Manisha provides connection)
        System.out.println("Finding vehicles for user: " + userId);
        return new ArrayList<>();
    }

    // Find vehicle by license plate
    public Vehicle findByLicensePlate(String licensePlate) {
        // TODO: Connect to DB (Manisha provides connection)
        System.out.println("Finding vehicle with plate: " + licensePlate);
        return null;
    }
}