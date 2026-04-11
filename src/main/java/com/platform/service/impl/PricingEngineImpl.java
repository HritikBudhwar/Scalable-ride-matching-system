package com.platform.service.impl;

import com.platform.model.enums.VehicleCategory;
import com.platform.model.payment.Invoice;
import com.platform.model.ride.BookingRequest;
import com.platform.model.ride.Trip;
import com.platform.repository.InvoiceRepository;
import com.platform.service.PricingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service Implementation for calculating fares and generating invoices.
 * Standardized to use VehicleCategory for pricing consistency.
 */
@Service
public class PricingEngineImpl implements PricingEngine {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public PricingEngineImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Double estimateFare(BookingRequest request) {
        double distance = calculateSimulatedDistance(request.getSource(), request.getDestination());
        
        // Use VehicleCategory from the request for the estimate
        VehicleCategory category = request.getVehicleCategory();
        
        double baseFare = getBaseFare(category);
        double perKmRate = getPerKmRate(category);
        
        double estimate = baseFare + (distance * perKmRate);
        
        // Return rounded to 2 decimal places
        return Math.round(estimate * 100.0) / 100.0;
    }

    @Override
    @Transactional
    public void generateInvoice(Trip trip) {
        // Use recorded distance if available, otherwise simulate
        double distance = trip.getDistanceTraveled() != null && trip.getDistanceTraveled() > 0 
                ? trip.getDistanceTraveled() 
                : calculateSimulatedDistance(trip.getSource(), trip.getDestination());
                
        // FIXED: Get VehicleCategory directly from the vehicle assigned to the trip
        VehicleCategory category = trip.getVehicle().getVehicleCategory();
        
        double baseFare = getBaseFare(category);
        double distanceCharge = distance * getPerKmRate(category);
        double timeCharge = 0.0; // Simulated placeholder
        double surgeMultiplier = 1.0; // Simulated placeholder
        
        double subtotal = (baseFare + distanceCharge + timeCharge) * surgeMultiplier;
        double tax = subtotal * 0.05; // 5% GST
        double totalAmount = subtotal + tax;

        // Create and populate the invoice
        Invoice invoice = new Invoice();
        invoice.setTrip(trip);
        invoice.setBaseFare(Math.round(baseFare * 100.0) / 100.0);
        invoice.setDistanceCharge(Math.round(distanceCharge * 100.0) / 100.0);
        invoice.setTimeCharge(timeCharge);
        invoice.setSurgeMultiplier(surgeMultiplier);
        invoice.setTax(Math.round(tax * 100.0) / 100.0);
        invoice.setTip(0.0);
        invoice.setTotalAmount(Math.round(totalAmount * 100.0) / 100.0);
        invoice.setPaid(false);
        invoice.setCreatedAt(LocalDateTime.now());

        // Save to database
        invoiceRepository.save(invoice);
        
        // Finalize the trip record with the calculated total fare
        trip.setTotalFare(invoice.getTotalAmount());
    }

    // ---- Helper Methods for Pricing Logic ----

    /**
     * Simulates distance between two points using deterministic hashing.
     */
    private double calculateSimulatedDistance(String source, String destination) {
        if (source == null || destination == null) return 10.0;
        
        int combinedHash = Math.abs(source.hashCode() ^ destination.hashCode());
        double distance = (combinedHash % 25) + 2.0; 
        
        return Math.round(distance * 10.0) / 10.0;
    }

    /**
     * Returns the base fare for a given vehicle category.
     */
    private double getBaseFare(VehicleCategory category) {
        if (category == null) return 50.0;
        switch (category) {
            case BIKE:      return 20.0;
            case AUTO:      return 35.0;
            case HATCHBACK: return 60.0;
            case SEDAN:     return 80.0;
            case SUV:       return 120.0;
            default:        return 50.0;
        }
    }

    /**
     * Returns the price-per-kilometer for a given vehicle category.
     */
    private double getPerKmRate(VehicleCategory category) {
        if (category == null) return 10.0;
        switch (category) {
            case BIKE:      return 6.0;
            case AUTO:      return 10.0;
            case HATCHBACK: return 14.0;
            case SEDAN:     return 16.0;
            case SUV:       return 22.0;
            default:        return 12.0;
        }
    }
}