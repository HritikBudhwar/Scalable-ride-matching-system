package com.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.platform.model.enums.TripStatus;
import com.platform.model.ride.Trip;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByCustomer(Customer customer);
    List<Trip> findByDriver(Driver driver);
    List<Trip> findByTripStatus(TripStatus status);
    Optional<Trip> findByDriverAndTripStatus(Driver driver, TripStatus status);
    List<Trip> findByCustomerOrderByCreatedAtDesc(Customer customer);
    List<Trip> findByDriverOrderByCreatedAtDesc(Driver driver);
}