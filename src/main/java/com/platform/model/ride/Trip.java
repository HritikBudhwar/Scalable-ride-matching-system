package com.platform.model.ride;

import com.platform.model.enums.TripStatus;
import com.platform.model.payment.Invoice;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Core transaction entity binding Customer, Driver, and Vehicle.
 *
 * Design Pattern: State — updateStatus() enforces valid transitions.
 * Invalid transitions throw IllegalStateException, matching the
 * finite state machine defined in the synopsis (section 5.1).
 *
 * Valid flow: REQUESTED → SEARCHING → ASSIGNED → IN_PROGRESS → COMPLETED | CANCELLED
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus tripStatus = TripStatus.REQUESTED;

    private String source;
    private String destination;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Double distanceTraveled = 0.0;

    // GPS path stored as JSON array string for simplicity
    private String routePath;
    @Column(nullable = true)
    private Boolean rideStartOtpVerified;

    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL)
    private Invoice invoice;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- State Machine ---

    /**
     * Enforces valid state transitions.
     * Throws IllegalStateException for invalid moves.
     */
    public void updateStatus(TripStatus newStatus) {
        if (!isValidTransition(this.tripStatus, newStatus)) {
            throw new IllegalStateException(
                "Invalid trip transition: " + this.tripStatus + " → " + newStatus
            );
        }
        this.tripStatus = newStatus;

        if (newStatus == TripStatus.IN_PROGRESS) {
            this.startTime = LocalDateTime.now();
        } else if (newStatus == TripStatus.COMPLETED || newStatus == TripStatus.CANCELLED) {
            this.endTime = LocalDateTime.now();
        }
    }

    private boolean isValidTransition(TripStatus from, TripStatus to) {
        switch (from) {
            case REQUESTED:   return to == TripStatus.SEARCHING   || to == TripStatus.CANCELLED;
            case SEARCHING:   return to == TripStatus.ASSIGNED    || to == TripStatus.CANCELLED;
            case ASSIGNED:    return to == TripStatus.IN_PROGRESS || to == TripStatus.CANCELLED;
            case IN_PROGRESS: return to == TripStatus.COMPLETED   || to == TripStatus.CANCELLED;
            default:          return false;
        }
    }

    public boolean isValidTransitionTo(TripStatus newStatus) {
        return isValidTransition(this.tripStatus, newStatus);
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public TripStatus getTripStatus() { return tripStatus; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public Double getDistanceTraveled() { return distanceTraveled; }
    public void setDistanceTraveled(Double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public String getRoutePath() { return routePath; }
    public void setRoutePath(String routePath) { this.routePath = routePath; }
    public boolean isRideStartOtpVerified() { return Boolean.TRUE.equals(rideStartOtpVerified); }
    public void setRideStartOtpVerified(boolean rideStartOtpVerified) { this.rideStartOtpVerified = rideStartOtpVerified; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    private Double totalFare;
    private Double estimatedFare;
    private Double estimatedDistance;

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public Double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(Double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }
}