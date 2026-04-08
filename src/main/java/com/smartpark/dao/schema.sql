
-- SmartPark Database Schema
-- Run this on a fresh MySQL install

DROP DATABASE IF EXISTS smartpark_db;
CREATE DATABASE smartpark_db;
USE smartpark_db;

CREATE TABLE locations (
    location_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL
);

CREATE TABLE users (
    user_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE parking_slots (
    slot_id VARCHAR(10) PRIMARY KEY,
    slot_type VARCHAR(20) NOT NULL,
    is_occupied BOOLEAN DEFAULT false,
    location_id VARCHAR(10),
    FOREIGN KEY (location_id) REFERENCES locations(location_id)
);

CREATE TABLE vehicles (
    vehicle_id VARCHAR(10) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL,
    owner_id VARCHAR(10),
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
);

CREATE TABLE bookings (
    booking_id VARCHAR(10) PRIMARY KEY,
    vehicle_id VARCHAR(10),
    slot_id VARCHAR(10),
    user_id VARCHAR(10),
    status VARCHAR(20) NOT NULL,
    check_in_time DATETIME,
    check_out_time DATETIME,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (slot_id) REFERENCES parking_slots(slot_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE payments (
    payment_id VARCHAR(10) PRIMARY KEY,
    booking_id VARCHAR(10),
    amount DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_time DATETIME,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

CREATE TABLE vehicle_history (
    id VARCHAR(10) PRIMARY KEY,
    vehicle_id VARCHAR(10) NOT NULL,
    slot_id VARCHAR(10) NOT NULL,
    check_in DATETIME NOT NULL,
    check_out DATETIME,
    duration_mins INT,
    amount DOUBLE,
    created_at DATETIME DEFAULT NOW(),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (slot_id) REFERENCES parking_slots(slot_id)
);

-- Indexes
ALTER TABLE bookings ADD INDEX idx_vehicle_id (vehicle_id);
ALTER TABLE bookings ADD INDEX idx_slot_id (slot_id);
ALTER TABLE payments ADD INDEX idx_booking_id (booking_id);
ALTER TABLE vehicles ADD INDEX idx_license_plate (license_plate);
ALTER TABLE vehicle_history ADD INDEX idx_vh_vehicle_id (vehicle_id);