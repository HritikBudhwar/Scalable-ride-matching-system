package com.platform.dto.response;

import com.platform.model.enums.TripStatus;
import com.platform.model.enums.VehicleCategory;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for trip responses
 */
public class TripResponseDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String pickupLocation;
    private String dropoffLocation;
    private VehicleCategory vehicleCategory;
    private String vehicleNumber;
    private Double estimatedFare;
    private Double actualFare;
    private Double estimatedDistance;
    private Double actualDistance;
    private TripStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String paymentStatus;
    private String paymentMethod;
    private Integer rating;
    private String feedback;
    private Double driverRating;
    private String emergencyStatus;
    
    public TripResponseDTO() {
        // Default constructor
    }
    
    public TripResponseDTO(Long id, Long customerId, String customerName, 
                         Long driverId, String driverName, String pickupLocation, 
                         String dropoffLocation, VehicleCategory vehicleCategory, 
                         Double estimatedFare, TripStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.vehicleCategory = vehicleCategory;
        this.estimatedFare = estimatedFare;
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
    
    public Long getDriverId() {
        return driverId;
    }
    
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
    
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    public Double getEstimatedFare() {
        return estimatedFare;
    }
    
    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }
    
    public Double getActualFare() {
        return actualFare;
    }
    
    public void setActualFare(Double actualFare) {
        this.actualFare = actualFare;
    }
    
    public Double getEstimatedDistance() {
        return estimatedDistance;
    }
    
    public void setEstimatedDistance(Double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }
    
    public Double getActualDistance() {
        return actualDistance;
    }
    
    public void setActualDistance(Double actualDistance) {
        this.actualDistance = actualDistance;
    }
    
    public TripStatus getStatus() {
        return status;
    }
    
    public void setStatus(TripStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }
    
    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public Double getDriverRating() {
        return driverRating;
    }
    
    public void setDriverRating(Double driverRating) {
        this.driverRating = driverRating;
    }
    
    public String getEmergencyStatus() {
        return emergencyStatus;
    }
    
    public void setEmergencyStatus(String emergencyStatus) {
        this.emergencyStatus = emergencyStatus;
    }
}
