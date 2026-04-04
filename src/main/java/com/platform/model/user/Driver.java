package com.platform.model.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

import com.platform.model.core.Matchable;
import com.platform.model.enums.DriverStatus;
import com.platform.model.enums.ServiceType;
import com.platform.model.enums.VehicleCategory;
import com.platform.model.ride.Vehicle;
import com.platform.model.support.Document;

/**
 * Represents a driver on the platform.
 * Inherits from User (JOINED inheritance strategy).
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "drivers")
@DiscriminatorValue("DRIVER")
public class Driver extends User implements Matchable {

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus = DriverStatus.PENDING_APPROVAL;

    // Current GPS position stored as "lat,lng"
    private String currentLocation;

    @Enumerated(EnumType.STRING)
    private ServiceType preferredType;

    @Enumerated(EnumType.STRING)
    private VehicleCategory preferredCategory;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "active_vehicle_id")
    private Vehicle activeVehicle;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    private Double totalEarnings = 0.0;

    // --- Getters and Setters ---
    // Explicitly defined to resolve "cannot find symbol" errors in Services/Controllers

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Vehicle getActiveVehicle() {
        return activeVehicle;
    }

    public void setActiveVehicle(Vehicle activeVehicle) {
        this.activeVehicle = activeVehicle;
    }

    public Double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    // --- Matchable implementation ---

    @Override
    public boolean isAvailable() {
        return driverStatus == DriverStatus.AVAILABLE && activeVehicle != null;
    }

    @Override
    public String getCurrentLocation() {
        return currentLocation;
    }
    
    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
    // --- Business Logic ---
    
    /**
     * Toggles driver between AVAILABLE and OFF_DUTY.
     * Enforces that drivers on a trip cannot go off-duty.
     */
    public void toggleAvailability() {
        if (driverStatus == DriverStatus.ON_TRIP) {
            throw new IllegalStateException("Cannot go available while on a trip.");
        }
        driverStatus = (driverStatus == DriverStatus.AVAILABLE)
                ? DriverStatus.OFF_DUTY
                : DriverStatus.AVAILABLE;
    }
}