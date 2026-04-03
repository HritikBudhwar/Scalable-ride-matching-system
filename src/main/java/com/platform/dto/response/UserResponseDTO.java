package com.platform.dto.response;

import com.platform.model.enums.Gender;
import com.platform.model.enums.VerificationStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for user responses
 */
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private String userType;
    private VerificationStatus verificationStatus;
    private String address;
    private String profilePicture;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime lastActive;
    private Boolean isActive;
    private String customerCode;
    private String driverCode;
    private String adminCode;
    
    public UserResponseDTO() {
        // Default constructor
    }
    
    public UserResponseDTO(Long id, String name, String email, String phone, 
                       Gender gender, String userType, VerificationStatus verificationStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.userType = userType;
        this.verificationStatus = verificationStatus;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }
    
    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastActive() {
        return lastActive;
    }
    
    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getCustomerCode() {
        return customerCode;
    }
    
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    
    public String getDriverCode() {
        return driverCode;
    }
    
    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }
    
    public String getAdminCode() {
        return adminCode;
    }
    
    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }
}
