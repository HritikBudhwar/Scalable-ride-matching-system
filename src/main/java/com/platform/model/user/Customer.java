package com.platform.model.user;

import com.platform.model.ride.BookingRequest;
import com.platform.model.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a passenger/customer.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    private String emergencyContact;

    

    @ElementCollection
    @CollectionTable(name = "customer_saved_addresses",
                     joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "address")
    private List<String> savedAddresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookingRequest> bookingHistory = new ArrayList<>();

    // --- Business Logic ---
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public boolean isSafetyProfileComplete() {
        // According to your logic: Female customers must have an emergency contact
        if (getGender() == Gender.FEMALE)  {
            return emergencyContact != null && !emergencyContact.isBlank();
        }
        return true; // Other genders don't have this restriction in current logic
    } 
}