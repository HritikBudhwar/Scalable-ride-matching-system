package com.platform.strategy.match.impl;

import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Driver;
import com.platform.strategy.match.MatchStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StandardMatchStrategy implements MatchStrategy {
    
    @Override
    public Driver matchDriver(BookingRequest request, List<Driver> availableDrivers) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }
        
        // Standard logic: Just pick the first available driver in the queue
        // (In a real production app, this would use geospatial distance)
        System.out.println("Standard Match: Selecting standard available driver.");
        return availableDrivers.get(0); 
    }
}