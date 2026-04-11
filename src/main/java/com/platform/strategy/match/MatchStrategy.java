package com.platform.strategy.match;

import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Driver;
import java.util.List;

public interface MatchStrategy {
    Driver matchDriver(BookingRequest request, List<Driver> availableDrivers);
}