
USE smartpark_db;

-- Seed locations
INSERT IGNORE INTO locations VALUES ('L001', 'Test Location', 'Delhi');

-- Seed users
INSERT IGNORE INTO users VALUES ('U001', 'Manisha', 'manisha@gmail.com', 'pass123', 'USER');
INSERT IGNORE INTO users VALUES ('U002', 'Rahul', 'rahul@gmail.com', 'pass123', 'USER');
INSERT IGNORE INTO users VALUES ('U003', 'Priya', 'priya@gmail.com', 'pass123', 'USER');

-- Seed parking slots
INSERT IGNORE INTO parking_slots VALUES ('S001', 'MEDIUM', false, 'L001');
INSERT IGNORE INTO parking_slots VALUES ('S002', 'SMALL', false, 'L001');
INSERT IGNORE INTO parking_slots VALUES ('S003', 'LARGE', false, 'L001');
INSERT IGNORE INTO parking_slots VALUES ('S004', 'MEDIUM', false, 'L001');
INSERT IGNORE INTO parking_slots VALUES ('S005', 'SMALL', false, 'L001');

-- Seed vehicles
INSERT IGNORE INTO vehicles VALUES ('V001', 'DL1234', 'CAR', 'U001');
INSERT IGNORE INTO vehicles VALUES ('V002', 'DL5678', 'BIKE', 'U002');
INSERT IGNORE INTO vehicles VALUES ('V003', 'DL9999', 'TRUCK', 'U003');

-- Seed bookings
INSERT IGNORE INTO bookings VALUES ('B001', 'V001', 'S001', 'U001', 'COMPLETED', '2026-04-01 08:00:00', '2026-04-01 10:00:00');
INSERT IGNORE INTO bookings VALUES ('B002', 'V002', 'S002', 'U002', 'COMPLETED', '2026-04-01 09:00:00', '2026-04-01 11:00:00');
INSERT IGNORE INTO bookings VALUES ('B003', 'V003', 'S003', 'U003', 'COMPLETED', '2026-04-01 10:00:00', '2026-04-01 12:00:00');

-- Seed payments
INSERT IGNORE INTO payments VALUES ('P001', 'B001', 100.00, 'COMPLETED', '2026-04-01 10:00:00');
INSERT IGNORE INTO payments VALUES ('P002', 'B002', 50.00, 'COMPLETED', '2026-04-01 11:00:00');
INSERT IGNORE INTO payments VALUES ('P003', 'B003', 200.00, 'COMPLETED', '2026-04-01 12:00:00');