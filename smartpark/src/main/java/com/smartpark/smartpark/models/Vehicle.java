package com.smartpark.smartpark.models;

/**
 * Abstract base class representing a vehicle in the SmartPark system.
 * Demonstrates abstraction and inheritance — all vehicle types (Car, Bike, Truck)
 * must extend this class and implement getVehicleType() and getParkingRate().
 *
 * @author Harnoor Kaur
 * @version 1.0
 */
public abstract class Vehicle {

    /** Vehicle license plate number */
    private String licensePlate;

    /** Name of the vehicle owner */
    private String ownerName;

    /** Phone number of the vehicle owner */
    private String ownerPhone;

    /**
     * Constructor to create a Vehicle object.
     *
     * @param licensePlate the vehicle's license plate number
     * @param ownerName the name of the owner
     * @param ownerPhone the phone number of the owner
     */
    public Vehicle(String licensePlate, String ownerName, String ownerPhone) {
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
    }

    /** Default constructor */
    public Vehicle() {}

    /**
     * Returns the type of vehicle.
     * Must be implemented by all subclasses.
     *
     * @return the vehicle type as a String (e.g., "Car", "Bike", "Truck")
     */
    public abstract String getVehicleType();

    /**
     * Returns the parking rate per hour for this vehicle type.
     * Must be implemented by all subclasses.
     *
     * @return the parking rate in rupees per hour
     */
    public abstract double getParkingRate();

    /**
     * Gets the license plate number.
     * @return the license plate
     */
    public String getLicensePlate() { return licensePlate; }

    /**
     * Sets the license plate number.
     * @param licensePlate the license plate to set
     */
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    /**
     * Gets the owner's name.
     * @return the owner name
     */
    public String getOwnerName() { return ownerName; }

    /**
     * Sets the owner's name.
     * @param ownerName the owner name to set
     */
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    /**
     * Gets the owner's phone number.
     * @return the owner phone
     */
    public String getOwnerPhone() { return ownerPhone; }

    /**
     * Sets the owner's phone number.
     * @param ownerPhone the owner phone to set
     */
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }

    /**
     * Returns a string representation of the Vehicle.
     * @return formatted string with vehicle details
     */
    @Override
    public String toString() {
        return getVehicleType() + " | Plate: " + licensePlate + " | Owner: " + ownerName;
    }
}