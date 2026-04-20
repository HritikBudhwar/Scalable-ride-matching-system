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
import com.platform.service.OTPManager;
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
    private final CustomerRepository customerRepository; 
    private final ParcelRepository parcelRepository;
    private final PricingEngine pricingEngine;
    private final NotificationService notificationService;
    private final OTPManager otpManager;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository,
                            DriverRepository driverRepository,
                            CustomerRepository customerRepository,
                            ParcelRepository parcelRepository,
                            PricingEngine pricingEngine,
                            NotificationService notificationService,
                            OTPManager otpManager) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.customerRepository = customerRepository;
        this.parcelRepository = parcelRepository;
        this.pricingEngine = pricingEngine;
        this.notificationService = notificationService;
        this.otpManager = otpManager;
    }

    @Override
    @Transactional
    public Trip acceptTrip(Long tripId, Long driverId) {
        Trip trip = getTrip(tripId);
        Driver driver = getDriver(driverId);

        // Marketplace flow:
        // - Drivers see SEARCHING trips (unassigned) via /api/trips/available
        // - The first driver to accept atomically claims the trip
        if (trip.getTripStatus() == TripStatus.SEARCHING) {
            if (trip.getDriver() != null) {
                throw new IllegalStateException("Trip has already been claimed by another driver.");
            }
            trip.setDriver(driver);
            trip.setVehicle(driver.getActiveVehicle());
            trip.updateStatus(TripStatus.ASSIGNED);
        } else if (trip.getTripStatus() == TripStatus.ASSIGNED) {
            // If already assigned, only the assigned driver can accept/start it
            if (trip.getDriver() == null || !trip.getDriver().getId().equals(driverId)) {
                throw new IllegalStateException("You are not assigned to this trip.");
            }
        } else {
            throw new IllegalStateException("Trip cannot be accepted in state: " + trip.getTripStatus());
        }

        // Update driver status to busy
        driver.setDriverStatus(DriverStatus.ON_TRIP);
        driverRepository.save(driver);

        String otpContext = buildRideStartOtpContext(trip.getId());
        String rideStartOtp = otpManager.generateOTP(otpContext);

        notificationService.notifyCustomer(trip.getCustomer().getId(),
            "Your driver " + driver.getFirstName() + " has accepted the ride. Share ride OTP " + rideStartOtp + " with the driver at pickup.");

        return tripRepository.save(trip);
    }

    @Override
    @Transactional
    public Trip verifyRideStartOtp(Long tripId, Long driverId, String otp) {
        Trip trip = getTrip(tripId);
        if (trip.getTripStatus() != TripStatus.ASSIGNED) {
            throw new IllegalStateException("Ride OTP can only be verified in ASSIGNED state.");
        }
        if (trip.getDriver() == null || !trip.getDriver().getId().equals(driverId)) {
            throw new IllegalStateException("You are not assigned to this trip.");
        }
        if (otp == null || otp.isBlank()) {
            throw new IllegalArgumentException("OTP is required.");
        }

        String otpContext = buildRideStartOtpContext(tripId);
        if (!otpManager.validateOTP(otpContext, otp.trim())) {
            throw new IllegalStateException("Invalid or expired OTP.");
        }

        otpManager.invalidateOTP(otpContext);
        trip.setRideStartOtpVerified(true);
        trip.updateStatus(TripStatus.IN_PROGRESS);

        notificationService.notifyCustomer(trip.getCustomer().getId(),
                "Your trip has started after OTP verification.");
        return tripRepository.save(trip);
    }

    @Override
    public String getRideStartOtpForCustomer(Long tripId, Long customerId) {
        Trip trip = getTrip(tripId);
        if (trip.getCustomer() == null || !trip.getCustomer().getId().equals(customerId)) {
            throw new IllegalStateException("This trip does not belong to the customer.");
        }
        if (trip.getTripStatus() != TripStatus.ASSIGNED || trip.isRideStartOtpVerified()) {
            throw new IllegalStateException("Ride OTP is not available for current trip state.");
        }

        String otp = otpManager.getOTP(buildRideStartOtpContext(tripId));
        if (otp == null) {
            throw new IllegalStateException("Ride OTP expired. Ask driver to re-accept trip.");
        }
        return otp;
    }

    @Override
    @Transactional
    public void rejectTrip(Long tripId, Long driverId) {
        Trip trip = getTrip(tripId);

        // Marketplace flow:
        // - Trips can be SEARCHING (unassigned) or ASSIGNED (claimed).
        // - Rejecting a SEARCHING trip is just "not interested" — no server state change needed.
        if (trip.getTripStatus() == TripStatus.SEARCHING && trip.getDriver() == null) {
            return;
        }

        // If the trip is already assigned, only the assigned driver can reject.
        if (trip.getDriver() == null || !trip.getDriver().getId().equals(driverId)) {
            throw new IllegalStateException("You are not assigned to this trip.");
        }

        // Put trip back to SEARCHING for another driver to claim.
        if (trip.getTripStatus() == TripStatus.ASSIGNED) {
            trip.updateStatus(TripStatus.SEARCHING);
            trip.setDriver(null);
            trip.setVehicle(null);
            tripRepository.save(trip);
            return;
        }

        throw new IllegalStateException("Trip cannot be rejected in state: " + trip.getTripStatus());
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

    @Override
    public List<Trip> getAvailableTrips() {
        // Get all trips in SEARCHING status (waiting for driver assignment)
        return tripRepository.findByTripStatus(TripStatus.SEARCHING);
    }

    @Override
    public Trip getDriverCurrentTrip(Long driverId) {
        Driver driver = getDriver(driverId);
        // Get driver's current trip if they have one and it's in progress
        return tripRepository.findByDriverAndTripStatus(driver, TripStatus.IN_PROGRESS)
                .orElseThrow(() -> new IllegalStateException("No active trip for driver: " + driverId));
    }

    // ---- Internal Logic for Final States ----

    private void onTripCompleted(Trip trip) {
        // Triggers the PricingEngine to finalize fare and generate the Invoice.
        // Wrapped in try-catch so a billing error never prevents the trip from being
        // saved as COMPLETED — the trip state is the source of truth.
        try {
            pricingEngine.generateInvoice(trip);
        } catch (Exception e) {
            System.err.println("[TripService] Invoice generation failed for trip " + trip.getId() + ": " + e.getMessage());
        }

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

    private String buildRideStartOtpContext(Long tripId) {
        return "TRIP_START_" + tripId;
    }
}