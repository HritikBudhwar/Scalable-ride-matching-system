package com.platform.dto.request;

import com.platform.model.enums.Gender;

/**
 * Data Transfer Object for user registration
 */
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
    private Gender gender;
    private String userType; // CUSTOMER, DRIVER, ADMIN
    private String address;
    private String profilePicture;
    
    public UserRegistrationDTO() {
        // Default constructor
    }
    
    public UserRegistrationDTO(String name, String email, String phone, String password, 
                          Gender gender, String userType, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.userType = userType;
        this.address = address;
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
}
