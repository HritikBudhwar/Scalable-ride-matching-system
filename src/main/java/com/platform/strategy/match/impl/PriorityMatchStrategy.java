package com.platform.strategy.match.impl;

import com.platform.model.enums.Gender;
import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Driver;
import com.platform.strategy.match.MatchStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PriorityMatchStrategy implements MatchStrategy {

    @Override
    public Driver matchDriver(BookingRequest request, List<Driver> availableDrivers) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }

        System.out.println("Priority Match: Prioritizing Female and high-rated drivers.");
        
        // Sorts the list so Gender.FEMALE is at the top, then sorts by rating descending
        return availableDrivers.stream()
                .sorted(Comparator
                    .comparing((Driver d) -> d.getGender() == Gender.FEMALE ? 0 : 1)
                    .thenComparing(Driver::getAverageRating, Comparator.nullsLast(Comparator.reverseOrder())))
                .findFirst()
                .orElse(availableDrivers.get(0));
    }
}