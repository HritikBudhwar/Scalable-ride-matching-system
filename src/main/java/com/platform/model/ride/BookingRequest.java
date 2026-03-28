package com.platform.model.ride;

import com.platform.model.enums.ServiceType;
import com.platform.model.enums.VehicleCategory;
import com.platform.model.user.Customer;

import java.time.LocalDateTime;

/**
 * Booking request entity class
 */
public class BookingRequest {
    private Long id;
    private Customer customer;
    private ServiceType serviceType;
    private VehicleCategory vehicleCategory;
    private String pickupLocation;
    private String dropoffLocation;
    private LocalDateTime requestedAt;
    private String specialInstructions;
}