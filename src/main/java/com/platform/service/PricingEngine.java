package com.platform.service;

import com.platform.model.ride.BookingRequest;
import com.platform.model.ride.Trip;

/**
 * Service for calculating ride pricing and generating invoices.
 */
public interface PricingEngine {
    
    Double estimateFare(BookingRequest request);
    
    void generateInvoice(Trip trip);

    Double calculateDistance(String source, String destination);
}