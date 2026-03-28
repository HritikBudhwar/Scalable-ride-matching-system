package com.platform.strategy;

import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Driver;

import java.util.List;

/**
 * Interface for driver matching strategies
 */
public interface MatchStrategy {
    List<Driver> findMatchingDrivers(BookingRequest bookingRequest);
}