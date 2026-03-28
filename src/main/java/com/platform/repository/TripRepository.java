package com.platform.repository;

import com.platform.model.ride.Trip;
import com.platform.model.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Trip entities
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByStatus(TripStatus status);
    List<Trip> findByCustomerId(Long customerId);
    List<Trip> findByDriverId(Long driverId);
}