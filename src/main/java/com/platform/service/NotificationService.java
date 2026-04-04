package com.platform.service;

import com.platform.model.ride.Trip;

/**
 * Service for handling platform notifications and safety alerts.
 */
public interface NotificationService {

    void notifyDriver(Long driverId, String message);

    void notifyCustomer(Long customerId, String message);

    void sendEmergencyTripAlert(Trip trip, String emergencyContact);
}