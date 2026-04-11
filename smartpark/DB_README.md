
# SmartPark Database Setup

## Requirements
- MySQL 8.0 or higher
- MySQL Workbench (recommended)

## Setup Instructions

### Step 1 - Run Schema
1. Open MySQL Workbench
2. Connect to your local instance
3. Open schema.sql file
4. Press Ctrl+Shift+Enter to run

### Step 2 - Run Seed Data
1. Open seed.sql file
2. Press Ctrl+Shift+Enter to run

## Database Credentials
- Host: localhost
- Port: 3306
- Database: smartpark_db
- Username: root
- Password: (your MySQL root password)

## Tables
- locations — parking locations
- users — registered users
- parking_slots — individual parking slots
- vehicles — registered vehicles
- bookings — parking bookings
- payments — payment records
- vehicle_history — vehicle visit history

## ER Relationships
- users → vehicles (one user has many vehicles)
- locations → parking_slots (one location has many slots)
- vehicles → bookings (one vehicle has many bookings)
- bookings → payments (one booking has one payment)
- vehicles → vehicle_history (one vehicle has many history records)