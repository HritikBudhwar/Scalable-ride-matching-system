package com.platform.model.payment;

import com.platform.model.ride.Trip;

import java.time.LocalDateTime;

/**
 * Invoice entity class
 */
public class Invoice {
    private Long id;
    private Trip trip;
    private double baseFare;
    private double tax;
    private double totalAmount;
    private LocalDateTime generatedAt;
    private LocalDateTime paidAt;
    private boolean isPaid;
}