package com.platform.dto.request;

import com.platform.model.enums.ServiceType;
import com.platform.model.enums.VehicleCategory;

/**
 * Data Transfer Object for booking requests
 */
public class BookingRequestDTO {
    private Long customerId;
    private ServiceType serviceType;
    private VehicleCategory vehicleCategory;
    private String pickupLocation;
    private String dropoffLocation;
    private String specialInstructions;
    private Double estimatedFare;
    
    public BookingRequestDTO() {
        // Default constructor
    }
    
    public BookingRequestDTO(Long customerId, ServiceType serviceType, VehicleCategory vehicleCategory, 
                         String pickupLocation, String dropoffLocation, String specialInstructions) {
        this.customerId = customerId;
        this.serviceType = serviceType;
        this.vehicleCategory = vehicleCategory;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.specialInstructions = specialInstructions;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public ServiceType getServiceType() {
        return serviceType;
    }
    
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
    public VehicleCategory getVehicleCategory() {
        return vehicleCategory;
    }
    
    public void setVehicleCategory(VehicleCategory vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
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
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    public Double getEstimatedFare() {
        return estimatedFare;
    }
    
    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }
}
