# SmartPark — Backend API Documentation

**Developer:** Harnoor Kaur (Member A — Backend Core)  
**Tech Stack:** Spring Boot 3.5 | MySQL 8.0 | Java 21 | Maven  
**Server:** Runs on `http://localhost:8080`

---

## Setup Instructions

### Prerequisites
- Java 21+
- Maven 3.9+
- MySQL 8.0

### Database Setup
```sql
CREATE DATABASE smartpark_db;
USE smartpark_db;
```
Run the schema to create all tables (users, locations, parking_slots, bookings, payments).

### Run the Server
```bash
cd smartpark
mvn spring-boot:run
```
Server starts on port 8080.

---

## API Endpoints

### Auth
| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | /api/auth/register | {username, email, password, role} | {status, message} |
| POST | /api/auth/login | {email, password} | {status, userId, username, role} |

### Slots
| Method | Endpoint | Response |
|--------|----------|----------|
| GET | /api/slots | All slots |
| GET | /api/slots/available | Available slots only |
| GET | /api/slots/{id}/check | Slot availability |

### Bookings
| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | /api/bookings | {userId, slotId, vehiclePlate} | {bookingId, status} |
| GET | /api/bookings/{id} | — | Booking details |
| GET | /api/bookings/user/{userId} | — | User's bookings |
| DELETE | /api/bookings/{id}/cancel | — | Cancellation status |

### Check-in / Check-out
| Method | Endpoint | Body | Response |
|--------|----------|------|----------|
| POST | /api/checkin | {bookingId} | {checkInTime, status} |
| POST | /api/checkout | {bookingId} | {totalAmount, status} |

### Reports
| Method | Endpoint | Response |
|--------|----------|----------|
| GET | /api/reports/summary | Total revenue and bookings |
| GET | /api/reports/revenue?period=daily | Daily revenue |
| GET | /api/reports/revenue?period=weekly | Weekly revenue |
| GET | /api/reports/revenue?period=monthly | Monthly revenue |
| GET | /api/reports/lot | Full lot report |

### Admin
| Method | Endpoint | Response |
|--------|----------|----------|
| GET | /api/admin/stats | Users, slots, revenue stats |
| GET | /api/admin/users | All users |
| GET | /api/admin/slots | All slots with location |

### QR Code
| Method | Endpoint | Response |
|--------|----------|----------|
| GET | /api/booking/{id}/qr | Base64 QR code image |

---

## OOP Concepts

| Concept | Implementation |
|---------|---------------|
| Classes & Objects | User, Vehicle, Booking, ParkingSlot, Payment, Location |
| Encapsulation | All fields private with getters/setters |
| Abstraction | abstract Vehicle with abstract getVehicleType() and getParkingRate() |
| Inheritance | Car, Bike, Truck extend Vehicle |
| Polymorphism | Vehicle[] array — one loop handles all types |
| Interfaces | Payable, Notifiable, Reportable |

---

## Security
- Passwords hashed with BCrypt before storing
- All SQL queries use PreparedStatement (no SQL injection)
- Input validation on all endpoints

---

*SmartPark — Full Stack Parking Management System*