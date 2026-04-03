package com.platform.dto.response;

import com.platform.model.enums.TripStatus;
import com.platform.model.enums.VehicleCategory;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for booking responses
 */
public class BookingResponseDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String pickupLocation;
    private String dropoffLocation;
    private VehicleCategory vehicleCategory;
    private Double fare;
    private TripStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedArrival;
    private String driverName;
    private String driverPhone;
    private String vehicleNumber;
    
    public BookingResponseDTO() {
        // Default constructor
    }
    
    public BookingResponseDTO(Long id, Long customerId, String customerName, 
                          String pickupLocation, String dropoffLocation, 
                          VehicleCategory vehicleCategory, Double fare, TripStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.vehicleCategory = vehicleCategory;
        this.fare = fare;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getPickupLocation() {
        return pickupLocation;
    }
    
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    
    public String getDropoffLocation() {
        return dropoffLocation;
    }
    
    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }
    
    public VehicleCategory getVehicleCategory() {
        return vehicleCategory;
    }
    
    public void setVehicleCategory(VehicleCategory vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }
    
    public Double getFare() {
        return fare;
    }
    
    public void setFare(Double fare) {
        this.fare = fare;
    }
    
    public TripStatus getStatus() {
        return status;
    }
    
    public void setStatus(TripStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getEstimatedArrival() {
        return estimatedArrival;
    }
    
    public void setEstimatedArrival(LocalDateTime estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }
    
    public String getDriverName() {
        return driverName;
    }
    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    
    public String getDriverPhone() {
        return driverPhone;
    }
    
    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
    
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
