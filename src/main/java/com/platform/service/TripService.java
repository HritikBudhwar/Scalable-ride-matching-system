package com.platform.service;

import java.util.List;

import com.platform.model.enums.TripStatus;
import com.platform.model.ride.Trip;

/**
 * Manages Trip state transitions and real-time updates.
 */
public interface TripService {

    /** Get all available trips for drivers to accept */
    List<Trip> getAvailableTrips();

    /** Get driver's current active trip */
    Trip getDriverCurrentTrip(Long driverId);

    /** Driver accepts the booking — transitions SEARCHING → ASSIGNED. */
    Trip acceptTrip(Long tripId, Long driverId);

    /** Driver rejects — trip goes back to SEARCHING for re-matching. */
    void rejectTrip(Long tripId, Long driverId);

    /** Driver updates trip state (ASSIGNED→IN_PROGRESS, IN_PROGRESS→COMPLETED, etc.). */
    Trip updateTripStatus(Long tripId, TripStatus newStatus);

    /** For parcel trips — driver submits delivery OTP to complete. */
    Trip completeParcelDelivery(Long tripId, String otp);

    /** Updates driver's live GPS location during the trip. */
    void updateDriverLocation(Long tripId, String geoPoint);

    Trip getTripById(Long tripId);

    List<Trip> getCustomerTrips(Long customerId);

    List<Trip> getDriverTrips(Long driverId);
}