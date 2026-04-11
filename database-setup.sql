-- PostgreSQL Database Setup for Ride Hailing Platform
-- Run this script to set up the database and user

-- Create database (run as postgres user)
-- CREATE DATABASE ride_hailing;

-- Create user (run as postgres user)
-- CREATE USER ride_hailing_user WITH PASSWORD 'your_secure_password';

-- Grant privileges (run as postgres user)
-- GRANT ALL PRIVILEGES ON DATABASE ride_hailing TO ride_hailing_user;

-- Connect to the ride_hailing database
-- \c ride_hailing;

-- Grant schema privileges
-- GRANT ALL ON SCHEMA public TO ride_hailing_user;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ride_hailing_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ride_hailing_user;

-- Optional: Create extensions for geospatial support (if needed for location features)
-- CREATE EXTENSION IF NOT EXISTS postgis;
-- CREATE EXTENSION IF NOT EXISTS postgis_topology;

-- Set default privileges for future tables
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO ride_hailing_user;
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO ride_hailing_user;
