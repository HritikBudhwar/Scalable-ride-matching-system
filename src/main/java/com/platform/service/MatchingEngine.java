package com.platform.service;

import com.platform.model.enums.DriverStatus;
import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Driver;
import com.platform.repository.DriverRepository;
import com.platform.strategy.match.MatchStrategy;
import com.platform.strategy.match.impl.PriorityMatchStrategy;
import com.platform.strategy.match.impl.StandardMatchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MatchingEngine {
    // Standardized method name to match your BookingServiceImpl calls
    Driver findMatch(BookingRequest request);
}