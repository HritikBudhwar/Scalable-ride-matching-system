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
        
        double subtotal = baseFare + (distance * perKmRate);
        double tax = subtotal * 0.05; // 5% GST — must match generateInvoice()
        double estimate = subtotal + tax;
        
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
                
        // Safe vehicle category fetch — Hibernate proxy may be non-null but back a null row,
        // so we use try-catch rather than a simple != null check.
        VehicleCategory category = null;
        try {
            if (trip.getVehicle() != null) {
                category = trip.getVehicle().getVehicleCategory();
            }
        } catch (Exception e) {
            // Vehicle proxy uninitialized or vehicle record missing → fall back to default rates
            category = null;
        }
        
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

        Double coordinateDistance = calculateCoordinateDistance(source, destination);
        if (coordinateDistance != null && coordinateDistance > 0) {
            return Math.round(coordinateDistance * 10.0) / 10.0;
        }

        int combinedHash = Math.abs(source.hashCode() ^ destination.hashCode());
        double distance = (combinedHash % 25) + 2.0;
        return Math.round(distance * 10.0) / 10.0;
    }

    @Override
    public Double calculateDistance(String source, String destination) {
        return calculateSimulatedDistance(source, destination);
    }

    private Double calculateCoordinateDistance(String source, String destination) {
        try {
            String[] sourceParts = source.split(",");
            String[] destinationParts = destination.split(",");
            if (sourceParts.length != 2 || destinationParts.length != 2) {
                return null;
            }

            double lat1 = Double.parseDouble(sourceParts[0].trim());
            double lon1 = Double.parseDouble(sourceParts[1].trim());
            double lat2 = Double.parseDouble(destinationParts[0].trim());
            double lon2 = Double.parseDouble(destinationParts[1].trim());

            double earthRadiusKm = 6371.0;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return earthRadiusKm * c;
        } catch (Exception ignored) {
            return null;
        }
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