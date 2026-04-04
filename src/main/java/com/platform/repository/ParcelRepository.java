package com.platform.repository;

import com.platform.model.ride.Parcel;
import com.platform.model.ride.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    Optional<Parcel> findByTrip(Trip trip);
}