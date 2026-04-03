package com.platform.dto.request;

import com.platform.model.enums.TripStatus;

/**
 * Data Transfer Object for trip updates
 */
public class TripUpdateDTO {
    private TripStatus status;
    private Double endLatitude;
    private Double endLongitude;
    private Double actualFare;
    private Double actualDistance;
    private String paymentMethod;
    private String feedback;
    private Integer rating;
    private String notes;
    private Long endTime;
    
    public TripUpdateDTO() {
        // Default constructor
    }
    
    public TripUpdateDTO(TripStatus status, Double endLatitude, Double endLongitude, 
                       Double actualFare, Double actualDistance) {
        this.status = status;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.actualFare = actualFare;
        this.actualDistance = actualDistance;
    }
    
    public TripStatus getStatus() {
        return status;
    }
    
    public void setStatus(TripStatus status) {
        this.status = status;
    }
    
    public Double getEndLatitude() {
        return endLatitude;
    }
    
    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }
    
    public Double getEndLongitude() {
        return endLongitude;
    }
    
    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }
    
    public Double getActualFare() {
        return actualFare;
    }
    
    public void setActualFare(Double actualFare) {
        this.actualFare = actualFare;
    }
    
    public Double getActualDistance() {
        return actualDistance;
    }
    
    public void setActualDistance(Double actualDistance) {
        this.actualDistance = actualDistance;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Long getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
