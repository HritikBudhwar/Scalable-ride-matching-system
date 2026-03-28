package com.platform.model.user;

import com.platform.model.core.Matchable;
import com.platform.model.enums.DriverStatus;
import com.platform.model.enums.VerificationStatus;
import com.platform.model.ride.Vehicle;

/**
 * Driver entity class
 */
public class Driver extends User implements Matchable {
    private String driverCode;
    private DriverStatus status;
    private VerificationStatus verificationStatus;
    private Vehicle vehicle;
    private double rating;
    private int totalTrips;
}