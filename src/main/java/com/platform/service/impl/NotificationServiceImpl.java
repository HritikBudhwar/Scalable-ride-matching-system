package com.platform.service.impl;

import com.platform.model.ride.Trip;
import com.platform.service.NotificationService;
import org.springframework.stereotype.Service;

/**
 * Stub notification service.
 * Production: replace with Firebase FCM for push notifications,
 * or integrate Twilio for SMS alerts.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void notifyDriver(Long driverId, String message) {
        System.out.println("[NOTIFY → DRIVER " + driverId + "] " + message);
    }

    @Override
    public void notifyCustomer(Long customerId, String message) {
        System.out.println("[NOTIFY → CUSTOMER " + customerId + "] " + message);
    }

    @Override
    public void sendEmergencyTripAlert(Trip trip, String emergencyContact) {
        // Synopsis section 1.1: send driver name, vehicle number, live location to emergency contact
        String driverName = trip.getDriver() != null
                ? trip.getDriver().getFirstName() + " " + trip.getDriver().getLastName()
                : "Unknown";
        String vehicleReg = (trip.getVehicle() != null)
                ? trip.getVehicle().getRegistrationNumber()
                : "Unknown";

        String message = String.format(
            "[SAFETY ALERT] %s has booked a ride. Driver: %s, Vehicle: %s. Track live: /track/%d",
            trip.getCustomer().getFirstName(), driverName, vehicleReg, trip.getId()
        );

        System.out.println("[EMERGENCY → " + emergencyContact + "] " + message);
        // Production: send SMS via Twilio/MSG91 to emergencyContact
    }
}