package com.platform.service.impl;

import com.platform.model.enums.DriverStatus;
import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Driver;
import com.platform.repository.DriverRepository;
import com.platform.service.MatchingEngine;
import com.platform.strategy.match.MatchStrategy;
import com.platform.strategy.match.impl.PriorityMatchStrategy;
import com.platform.strategy.match.impl.StandardMatchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Spring manages the implementation, not the interface
public class MatchingEngineImpl implements MatchingEngine {

    private final DriverRepository driverRepository;
    private final StandardMatchStrategy standardMatchStrategy;
    private final PriorityMatchStrategy priorityMatchStrategy;

    @Autowired
    public MatchingEngineImpl(DriverRepository driverRepository, 
                              StandardMatchStrategy standardMatchStrategy, 
                              PriorityMatchStrategy priorityMatchStrategy) {
        this.driverRepository = driverRepository;
        this.standardMatchStrategy = standardMatchStrategy;
        this.priorityMatchStrategy = priorityMatchStrategy;
    }

    @Override
    public Driver findMatch(BookingRequest request) {
        // 1. Fetch available drivers using your custom repository query
        List<Driver> availableDrivers = driverRepository.findAvailableByServiceType(
                DriverStatus.AVAILABLE, 
                request.getServiceType()
        );

        if (availableDrivers.isEmpty()) {
            return null; // BookingServiceImpl will handle the null check
        }

        // 2. Select strategy dynamically based on vehicle category
        MatchStrategy strategy = selectStrategy(request);

        // 3. Delegate to the strategy to find the best driver
        return strategy.matchDriver(request, availableDrivers);
    }

    private MatchStrategy selectStrategy(BookingRequest request) {
        // Logic: Premium categories get the Priority (Rating/Gender) matching
        String category = request.getServiceType().name();
        if ("SEDAN".equals(category) || "SUV".equals(category)) {
            return priorityMatchStrategy;
        }
        return standardMatchStrategy;
    }
}