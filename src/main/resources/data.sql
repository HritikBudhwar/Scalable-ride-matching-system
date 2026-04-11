-- Sample data for H2 database
-- This will be loaded automatically when the application starts

-- Insert sample users
INSERT INTO users (id, first_name, last_name, email, phone, password, user_type, created_at) VALUES 
(1, 'John', 'Doe', 'john.doe@example.com', '+1234567890', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'CUSTOMER', CURRENT_TIMESTAMP),
(2, 'Jane', 'Smith', 'jane.smith@example.com', '+0987654321', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'DRIVER', CURRENT_TIMESTAMP),
(3, 'Mike', 'Wilson', 'mike.wilson@example.com', '+1122334455', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'DRIVER', CURRENT_TIMESTAMP),
(4, 'Sarah', 'Johnson', 'sarah.johnson@example.com', '+1555666777', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'CUSTOMER', CURRENT_TIMESTAMP);

-- Insert sample drivers
INSERT INTO drivers (id, user_id, license_number, driver_status, verification_status, rating, total_trips, created_at) VALUES 
(2, 2, 'DL123456', 'OFFLINE', 'VERIFIED', 4.8, 152, CURRENT_TIMESTAMP),
(3, 3, 'DL789012', 'ONLINE', 'VERIFIED', 4.6, 89, CURRENT_TIMESTAMP);

-- Insert sample vehicles
INSERT INTO vehicles (id, driver_id, make, model, vehicle_year, registration_number, vehicle_category, created_at) VALUES 
(1, 2, 'Toyota', 'Camry', 2020, 'ABC-1234', 'STANDARD', CURRENT_TIMESTAMP),
(2, 3, 'Honda', 'Accord', 2021, 'XYZ-5678', 'COMFORT', CURRENT_TIMESTAMP);

-- Insert sample customers
INSERT INTO customers (id, user_id, customer_status, total_rides, created_at) VALUES 
(1, 1, 'ACTIVE', 23, CURRENT_TIMESTAMP),
(4, 4, 'ACTIVE', 45, CURRENT_TIMESTAMP);

-- Insert sample booking requests
INSERT INTO booking_requests (id, customer_id, source, destination, service_type, status, estimated_fare, created_at) VALUES 
(1, 1, '123 Main Street', 'Airport Terminal 1', 'STANDARD', 'CONFIRMED', 25.50, CURRENT_TIMESTAMP),
(2, 4, '456 Oak Avenue', 'Downtown Mall', 'COMFORT', 'SEARCHING', 18.75, CURRENT_TIMESTAMP),
(3, 1, '789 Pine Road', 'Central Station', 'PREMIUM', 'CANCELLED', 35.00, CURRENT_TIMESTAMP);

-- Insert sample trips
INSERT INTO trips (id, booking_request_id, customer_id, driver_id, vehicle_id, source, destination, trip_status, start_time, end_time, distance_traveled, created_at) VALUES 
(1, 1, 1, 2, 1, '123 Main Street', 'Airport Terminal 1', 'COMPLETED', 
   TIMESTAMP '2024-01-15 14:30:00', TIMESTAMP '2024-01-15 15:15:00', 12.5, CURRENT_TIMESTAMP),
(2, 3, 1, 3, 2, '789 Pine Road', 'Central Station', 'CANCELLED', 
   TIMESTAMP '2024-01-15 16:20:00', NULL, 0.0, CURRENT_TIMESTAMP);

-- Insert sample invoices
INSERT INTO invoices (id, trip_id, base_fare, distance_fare, time_fare, total_amount, payment_status, created_at) VALUES 
(1, 1, 10.00, 15.50, 0.00, 25.50, 'PAID', CURRENT_TIMESTAMP);

-- Insert sample ratings
INSERT INTO ratings (id, trip_id, customer_id, driver_id, rating, comment, created_at) VALUES 
(1, 1, 1, 2, 5, 'Great ride, very professional!', CURRENT_TIMESTAMP);
