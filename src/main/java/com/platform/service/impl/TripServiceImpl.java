package com.platform.service.impl;

import com.platform.model.enums.DriverStatus;
import com.platform.model.enums.TripStatus;
import com.platform.model.ride.Parcel;
import com.platform.model.ride.Trip;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.repository.CustomerRepository;
import com.platform.repository.DriverRepository;
import com.platform.repository.ParcelRepository;
import com.platform.repository.TripRepository;
import com.platform.service.NotificationService;
import com.platform.service.PricingEngine;
import com.platform.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final CustomerRepository customerRepository; // Added missing dependency
    private final ParcelRepository parcelRepository;
    private final PricingEngine pricingEngine;
    private final NotificationService notificationService;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository,
                            DriverRepository driverRepository,
                            CustomerRepository customerRepository,
                            ParcelRepository parcelRepository,
                            PricingEngine pricingEngine,
                            NotificationService notificationService) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.customerRepository = customerRepository;
        this.parcelRepository = parcelRepository;
        this.pricingEngine = pricingEngine;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Trip acceptTrip(Long tripId, Long driverId) {
        Trip trip = getTrip(tripId);
        Driver driver = getDriver(driverId);

        if (trip.getTripStatus() != TripStatus.ASSIGNED) {
            throw new IllegalStateException("Trip is not in ASSIGNED state.");
        }
        
        // Concurrency guard: only the assigned driver can accept
        if (trip.getDriver() == null || !trip.getDriver().getId().equals(driverId)) {
            throw new IllegalStateException("You are not assigned to this trip.");
        }

        // Update driver status to busy
        driver.setDriverStatus(DriverStatus.ON_TRIP);
        driverRepository.save(driver);

        notificationService.notifyCustomer(trip.getCustomer().getId(),
            "Your driver " + driver.getFirstName() + " has accepted the ride and is on the way!");

        return tripRepository.save(trip);
    }

    @Override
    @Transactional
    public void rejectTrip(Long tripId, Long driverId) {
        Trip trip = getTrip(tripId);

        if (trip.getDriver() == null || !trip.getDriver().getId().equals(driverId)) {
            throw new IllegalStateException("You are not assigned to this trip.");
        }

        // Put trip back to SEARCHING for the MatchingEngine to pick up again
        trip.updateStatus(TripStatus.SEARCHING);
        trip.setDriver(null);
        tripRepository.save(trip);
    }

    @Override
    @Transactional
    public Trip updateTripStatus(Long tripId, TripStatus newStatus) {
        Trip trip = getTrip(tripId);

        // State machine logic is strictly enforced inside the Trip entity's updateStatus()
        trip.updateStatus(newStatus);

        if (newStatus == TripStatus.COMPLETED) {
            onTripCompleted(trip);
        } else if (newStatus == TripStatus.CANCELLED) {
            onTripCancelled(trip);
        }

        return tripRepository.save(trip);
    }

    @Override
    @Transactional
    public Trip completeParcelDelivery(Long tripId, String otp) {
        Trip trip = getTrip(tripId);

        Parcel parcel = parcelRepository.findByTrip(trip)
                .orElseThrow(() -> new IllegalStateException("No parcel associated with this trip ID: " + tripId));

        // OTP Handshake verification
        if (!parcel.verifyOtp(otp)) {
            throw new IllegalStateException("Invalid delivery OTP. Security verification failed.");
        }

        trip.updateStatus(TripStatus.COMPLETED);
        onTripCompleted(trip);

        return tripRepository.save(trip);
    }

    @Override
    @Transactional
    public void updateDriverLocation(Long tripId, String geoPoint) {
        Trip trip = getTrip(tripId);
        if (trip.getDriver() != null) {
            trip.getDriver().setCurrentLocation(geoPoint);
            driverRepository.save(trip.getDriver());
        }
    }

    @Override
    public Trip getTripById(Long tripId) {
        return getTrip(tripId);
    }

    @Override
    public List<Trip> getCustomerTrips(Long customerId) {
        // FIXED: Now correctly finds the customer first to fetch their trip list
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        return tripRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    @Override
    public List<Trip> getDriverTrips(Long driverId) {
        Driver driver = getDriver(driverId);
        return tripRepository.findByDriverOrderByCreatedAtDesc(driver);
    }

    // ---- Internal Logic for Final States ----

    private void onTripCompleted(Trip trip) {
        // Triggers the PricingEngine to finalize fare and generate the Invoice
        pricingEngine.generateInvoice(trip);

        // Reset driver availability
        Driver driver = trip.getDriver();
        if (driver != null) {
            driver.setDriverStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }

        notificationService.notifyCustomer(trip.getCustomer().getId(),
            "Arrived! Trip completed. You can now view your invoice and settle payment.");
    }

    private void onTripCancelled(Trip trip) {
        Driver driver = trip.getDriver();
        if (driver != null) {
            driver.setDriverStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
        }
    }

    private Trip getTrip(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip record not found for ID: " + tripId));
    }

    private Driver getDriver(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver record not found for ID: " + driverId));
    }
}