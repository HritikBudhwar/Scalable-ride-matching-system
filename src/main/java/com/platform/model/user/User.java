package com.platform.model.user;

import lombok.Data;
import com.platform.model.core.Rateable;
import com.platform.model.enums.Gender;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Base user entity class representing the User block in the class diagram.
 */

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Rateable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Keeping Long for JPA auto-generation compatibility

    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone; 
    private String email;
    private Date dob;
    private String profilePictureUrl;
    
    private Double walletBalance = 0.0;
    
    private boolean isActive = true; 
    private float averageRating = 0f;

    // MERGED: Keep only one declaration with the annotation
    @Enumerated(EnumType.STRING)
    private Gender gender; 

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ADDED: Manual getter so Customer.java can definitely find it
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }
    // ... rest of the file

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    // Add these inside your User class
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }



    public float getAverageRating() {
        return averageRating;
    }

}