package com.platform.controller;

import com.platform.dto.request.BookingRequestDTO;
import com.platform.dto.response.BookingResponseDTO;
import com.platform.dto.response.TripResponseDTO;
import com.platform.model.ride.BookingRequest;
import com.platform.model.ride.Trip;
import com.platform.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles the booking flow: create → confirm → cancel.
 * MVC Pattern: Controller — thin layer, all logic in BookingService.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Step 1: Customer creates a booking (gets back fare estimate).
     * POST /api/bookings?customerId=1
     */
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO dto,
            @RequestParam Long customerId) {
        BookingRequest booking = bookingService.createBooking(dto, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toBookingDTO(booking));
    }

    /**
     * Step 2: Customer confirms — triggers MatchingEngine, creates Trip.
     * POST /api/bookings/{id}/confirm
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<TripResponseDTO> confirmBooking(@PathVariable Long id) {
        Trip trip = bookingService.confirmBooking(id);
        return ResponseEntity.ok(toTripDTO(trip));
    }

    /**
     * Customer cancels before driver assigned.
     * POST /api/bookings/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Map<String, String>> cancelBooking(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String reason = (body != null) ? body.getOrDefault("reason", "No reason provided") : "";
        bookingService.cancelBooking(id, reason);
        return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully."));
    }

    /**
     * GET /api/bookings?customerId=1
     */
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getCustomerBookings(
            @RequestParam Long customerId) {
        List<BookingResponseDTO> result = bookingService.getCustomerBookings(customerId)
                .stream().map(this::toBookingDTO).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(toBookingDTO(bookingService.getBookingById(id)));
    }

    // ---- Mappers ----

    private BookingResponseDTO toBookingDTO(BookingRequest b) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(b.getId());
        dto.setSource(b.getSource());
        dto.setDestination(b.getDestination());
        dto.setServiceType(b.getServiceType());
        dto.setStatus(b.getStatus());
        dto.setEstimatedFare(b.getEstimatedFare());
        return dto;
    }

    private TripResponseDTO toTripDTO(Trip t) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.setId(t.getId());
        dto.setSource(t.getSource());
        dto.setDestination(t.getDestination());
        dto.setStatus(t.getTripStatus());
        if (t.getCustomer() != null) {
            dto.setCustomerName(t.getCustomer().getFirstName() + " " + t.getCustomer().getLastName());
            dto.setCustomerPhone(t.getCustomer().getPhone());
        }
        if (t.getDriver() != null) {
            dto.setDriverName(t.getDriver().getFirstName() + " " + t.getDriver().getLastName());
            dto.setDriverPhone(t.getDriver().getPhone());
        }
        if (t.getVehicle() != null) {
            dto.setVehicleReg(t.getVehicle().getRegistrationNumber());
            dto.setVehicleModel(t.getVehicle().getModel());
        }
        dto.setStartTime(t.getStartTime());
        dto.setEndTime(t.getEndTime());
        dto.setDistanceTraveled(t.getDistanceTraveled());
        return dto;
    }
}