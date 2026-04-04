package com.platform.model.ride;

import com.platform.model.enums.ServiceType;
import com.platform.model.enums.VehicleCategory;
import com.platform.model.user.Driver;

import javax.persistence.*;

/**
 * Physical asset owned by a Driver.
 * A Driver may own many Vehicles but only one is active per session.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String model;

    private String color;

    private int capacity;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private VehicleCategory vehicleCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    /**
     * Checks whether this vehicle can handle a given service type.
     * e.g. BIKE cannot carry 4 passengers.
     */
    public boolean canHandle(ServiceType requestedType) {
        return this.serviceType == requestedType;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public VehicleCategory getVehicleCategory() { return vehicleCategory; }
    public void setVehicleCategory(VehicleCategory vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    
}