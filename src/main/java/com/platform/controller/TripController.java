package com.platform.controller;

import com.platform.dto.request.TripUpdateDTO;
import com.platform.dto.response.TripResponseDTO;
import com.platform.model.ride.Trip;
import com.platform.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages trip state transitions and real-time driver actions.
 * MVC Pattern: Controller — delegates all state logic to TripService.
 */
@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Driver accepts an assigned trip.
     * POST /api/trips/{id}/accept?driverId=5
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<TripResponseDTO> acceptTrip(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        Trip trip = tripService.acceptTrip(id, driverId);
        return ResponseEntity.ok(toDTO(trip));
    }

    /**
     * Driver rejects a trip — triggers re-matching.
     * POST /api/trips/{id}/reject?driverId=5
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectTrip(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        tripService.rejectTrip(id, driverId);
        return ResponseEntity.ok(Map.of("message", "Trip rejected. Re-matching in progress."));
    }

    /**
     * Update trip status (IN_PROGRESS, COMPLETED, CANCELLED).
     * PUT /api/trips/{id}/status
     * Body: { "newStatus": "IN_PROGRESS" }
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TripResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody TripUpdateDTO dto) {
        Trip trip = tripService.updateTripStatus(id, dto.getNewStatus());
        return ResponseEntity.ok(toDTO(trip));
    }

    /**
     * Complete a parcel delivery with OTP verification.
     * POST /api/trips/{id}/complete-parcel
     * Body: { "otp": "482910" }
     */
    @PostMapping("/{id}/complete-parcel")
    public ResponseEntity<TripResponseDTO> completeParcel(
            @PathVariable Long id,
            @RequestBody TripUpdateDTO dto) {
        Trip trip = tripService.completeParcelDelivery(id, dto.getOtp());
        return ResponseEntity.ok(toDTO(trip));
    }

    /**
     * Driver updates live GPS location during trip.
     * PUT /api/trips/{id}/location
     * Body: { "geoPoint": "12.9716,77.5946" }
     */
    @PutMapping("/{id}/location")
    public ResponseEntity<Map<String, String>> updateLocation(
            @PathVariable Long id,
            @RequestBody TripUpdateDTO dto) {
        tripService.updateDriverLocation(id, dto.getGeoPoint());
        return ResponseEntity.ok(Map.of("message", "Location updated."));
    }

    /**
     * GET /api/trips/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTrip(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(tripService.getTripById(id)));
    }

    /**
     * GET /api/trips?customerId=1  OR  ?driverId=2
     */
    @GetMapping
    public ResponseEntity<List<TripResponseDTO>> getTrips(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long driverId) {

        List<Trip> trips;
        if (customerId != null) {
            trips = tripService.getCustomerTrips(customerId);
        } else if (driverId != null) {
            trips = tripService.getDriverTrips(driverId);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
            trips.stream().map(this::toDTO).collect(Collectors.toList())
        );
    }

    // ---- Mapper ----
    private TripResponseDTO toDTO(Trip t) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.setId(t.getId());
        dto.setSource(t.getSource());
        dto.setDestination(t.getDestination());
        dto.setStatus(t.getTripStatus());
        dto.setDistanceTraveled(t.getDistanceTraveled());
        dto.setStartTime(t.getStartTime());
        dto.setEndTime(t.getEndTime());
        if (t.getDriver() != null) {
            dto.setDriverName(t.getDriver().getFirstName() + " " + t.getDriver().getLastName());
        }
        if (t.getVehicle() != null) {
            // Updated to match our model's "RegistrationNo"
            dto.setVehicleReg(t.getVehicle().getRegistrationNumber());
            dto.setVehicleModel(t.getVehicle().getModel());
        }
        if (t.getInvoice() != null) {
            dto.setInvoiceTotal(t.getInvoice().getTotalAmount());
        }
        return dto;
    }
}