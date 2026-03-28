package com.platform.model.ride;

import com.platform.model.core.Payable;
import com.platform.model.enums.TripStatus;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;

import java.time.LocalDateTime;

/**
 * Trip entity class
 */
public class Trip implements Payable {
    private Long id;
    private Customer customer;
    private Driver driver;
    private String pickupLocation;
    private String dropoffLocation;
    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private TripStatus status;
    private double fare;
    private double distance;
}