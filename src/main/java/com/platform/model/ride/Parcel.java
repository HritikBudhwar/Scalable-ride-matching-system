package com.platform.model.ride;

import com.platform.model.enums.ParcelStatus;
import com.platform.model.user.Customer;

import java.time.LocalDateTime;

/**
 * Parcel entity class
 */
public class Parcel {
    private Long id;
    private Customer sender;
    private Customer recipient;
    private String pickupAddress;
    private String deliveryAddress;
    private ParcelStatus status;
    private double weight;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
}