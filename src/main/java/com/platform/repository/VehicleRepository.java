package com.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.platform.model.enums.ServiceType;
import com.platform.model.ride.Vehicle;
import com.platform.model.user.Driver;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    List<Vehicle> findByDriver(Driver driver);
    List<Vehicle> findByServiceType(ServiceType serviceType);
}