package com.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.platform.model.enums.DriverStatus;
import com.platform.model.enums.Gender;
import com.platform.model.enums.ServiceType;
import com.platform.model.user.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    Optional<Driver> findByPhone(String phone);

    List<Driver> findByDriverStatus(DriverStatus status);

    // Used by PriorityMatchStrategy — fetch available female drivers first
    List<Driver> findByDriverStatusAndGender(DriverStatus status, Gender gender);

    // Used by MatchingEngine — fetch all available drivers for a service type
    @Query("SELECT d FROM Driver d JOIN d.activeVehicle v " +
           "WHERE d.driverStatus = :status AND v.serviceType = :serviceType")
    List<Driver> findAvailableByServiceType(DriverStatus status, ServiceType serviceType);

   
}