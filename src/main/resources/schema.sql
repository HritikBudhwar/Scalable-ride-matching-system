-- H2 Database Schema for Ride Hailing Platform
-- This schema will be created automatically when the application starts

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('CUSTOMER', 'DRIVER', 'ADMINISTRATOR')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drivers table
CREATE TABLE IF NOT EXISTS drivers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    driver_status VARCHAR(20) DEFAULT 'OFFLINE' CHECK (driver_status IN ('ONLINE', 'OFFLINE', 'BUSY', 'SUSPENDED')),
    verification_status VARCHAR(20) DEFAULT 'PENDING' CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    rating DECIMAL(3,2) DEFAULT 0.0,
    total_trips INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    vehicle_year INTEGER NOT NULL,
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    vehicle_category VARCHAR(20) NOT NULL CHECK (vehicle_category IN ('STANDARD', 'COMFORT', 'PREMIUM', 'BIKE')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    customer_status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (customer_status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    total_rides INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Booking requests table
CREATE TABLE IF NOT EXISTS booking_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    service_type VARCHAR(20) NOT NULL CHECK (service_type IN ('STANDARD', 'COMFORT', 'PREMIUM', 'BIKE')),
    status VARCHAR(20) DEFAULT 'SEARCHING' CHECK (status IN ('SEARCHING', 'CONFIRMED', 'CANCELLED')),
    estimated_fare DECIMAL(10,2),
    estimated_time INTEGER, -- in minutes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trips table
CREATE TABLE IF NOT EXISTS trips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_request_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    driver_id BIGINT,
    vehicle_id BIGINT,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    trip_status VARCHAR(20) DEFAULT 'SEARCHING' CHECK (trip_status IN ('SEARCHING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    distance_traveled DECIMAL(10,2) DEFAULT 0.0, -- in kilometers
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    base_fare DECIMAL(10,2) NOT NULL,
    distance_fare DECIMAL(10,2) DEFAULT 0.0,
    time_fare DECIMAL(10,2) DEFAULT 0.0,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'PENDING' CHECK (payment_status IN ('PENDING', 'PAID', 'REFUNDED')),
    payment_method VARCHAR(20) DEFAULT 'WALLET' CHECK (payment_method IN ('WALLET', 'UPI', 'CASH')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ratings table
CREATE TABLE IF NOT EXISTS ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tables will be created with basic indexes by JPA
-- We'll add custom indexes later once basic functionality works
