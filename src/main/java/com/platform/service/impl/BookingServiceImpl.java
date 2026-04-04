package com.platform.service.impl;

import com.platform.dto.request.BookingRequestDTO;
import com.platform.model.enums.TripStatus;
import com.platform.model.ride.BookingRequest;
import com.platform.model.ride.Trip;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.repository.BookingRequestRepository;
import com.platform.repository.CustomerRepository;
import com.platform.repository.TripRepository;
import com.platform.service.BookingService;
import com.platform.service.MatchingEngine;
import com.platform.service.NotificationService;
import com.platform.service.PricingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRequestRepository bookingRepo;
    private final CustomerRepository customerRepository;
    private final TripRepository tripRepository;
    private final MatchingEngine matchingEngine;
    private final PricingEngine pricingEngine;
    private final NotificationService notificationService;

    @Autowired
    public BookingServiceImpl(BookingRequestRepository bookingRepo,
                               CustomerRepository customerRepository,
                               TripRepository tripRepository,
                               MatchingEngine matchingEngine,
                               PricingEngine pricingEngine,
                               NotificationService notificationService) {
        this.bookingRepo = bookingRepo;
        this.customerRepository = customerRepository;
        this.tripRepository = tripRepository;
        this.matchingEngine = matchingEngine;
        this.pricingEngine = pricingEngine;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public BookingRequest createBooking(BookingRequestDTO dto, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        if (!customer.isSafetyProfileComplete()) {
            throw new IllegalStateException(
                "Female customers must register an emergency contact before booking.");
        }

        BookingRequest request = new BookingRequest();
        request.setCustomer(customer);
        request.setSource(dto.getSource());
        request.setDestination(dto.getDestination());
        request.setStopPoints(dto.getStopPoints());
        request.setServiceType(dto.getServiceType());
        request.setVehicleCategory(dto.getVehicleCategory());
        request.setScheduledTime(dto.getScheduledTime());
        request.setStatus(TripStatus.REQUESTED);

        double estimate = pricingEngine.estimateFare(request);
        request.setEstimatedFare(estimate);

        return bookingRepo.save(request);
    }

    @Override
    @Transactional
    public Trip confirmBooking(Long bookingRequestId) {
        BookingRequest booking = bookingRepo.findById(bookingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingRequestId));

        if (booking.getStatus() != TripStatus.REQUESTED) {
            throw new IllegalStateException("Booking is not in REQUESTED state.");
        }

        booking.setStatus(TripStatus.SEARCHING);
        bookingRepo.save(booking);

        // CORRECTED: Now calls findMatch() to align with our MatchingEngine implementation
        Driver driver = matchingEngine.findMatch(booking);
        
        if (driver == null) {
            booking.setStatus(TripStatus.CANCELLED);
            bookingRepo.save(booking);
            throw new IllegalStateException("No drivers available. Please try again.");
        }

        Trip trip = new Trip();
        trip.setCustomer(booking.getCustomer());
        trip.setDriver(driver);
        trip.setVehicle(driver.getActiveVehicle());
        trip.setSource(booking.getSource());
        trip.setDestination(booking.getDestination());
        trip.updateStatus(TripStatus.SEARCHING);
        trip.updateStatus(TripStatus.ASSIGNED);

        booking.setStatus(TripStatus.ASSIGNED);
        bookingRepo.save(booking);

        Trip savedTrip = tripRepository.save(trip);

        notificationService.notifyDriver(driver.getId(),
            "New trip assigned! Customer: " + booking.getCustomer().getFirstName());

        String emergencyContact = booking.getCustomer().getEmergencyContact();
        if (emergencyContact != null && !emergencyContact.isBlank()) {
            notificationService.sendEmergencyTripAlert(savedTrip, emergencyContact);
        }

        return savedTrip;
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingRequestId, String reason) {
        BookingRequest booking = bookingRepo.findById(bookingRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingRequestId));

        if (booking.getStatus() == TripStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking.");
        }

        booking.setStatus(TripStatus.CANCELLED);
        bookingRepo.save(booking);
    }

    @Override
    public List<BookingRequest> getCustomerBookings(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        return bookingRepo.findByCustomerOrderByCreatedAtDesc(customer);
    }

    @Override
    public BookingRequest getBookingById(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
    }
}