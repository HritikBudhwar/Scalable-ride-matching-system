package com.platform.model.ride;

import com.platform.model.enums.ServiceType;
import com.platform.model.enums.TripStatus;
import com.platform.model.enums.VehicleCategory;
import com.platform.model.user.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Captures the customer's intent before a Trip is created.
 * Lifecycle: BookingRequest → (MatchingEngine) → Trip
 *
 * Design Pattern: Factory — BookingService acts as a factory
 * that creates a Trip from a confirmed BookingRequest.
 */
@Entity
@Table(name = "booking_requests")
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String source;          // "lat,lng"

    @Column(nullable = false)
    private String destination;     // "lat,lng"

    private String stopPoints;      // comma-separated "lat,lng" waypoints

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private VehicleCategory vehicleCategory;

    @Enumerated(EnumType.STRING)
    private TripStatus status = TripStatus.REQUESTED;

    private LocalDateTime scheduledTime;   // null = ride now

    private LocalDateTime createdAt;

    private Double estimatedFare;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getStopPoints() { return stopPoints; }
    public void setStopPoints(String stopPoints) { this.stopPoints = stopPoints; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public VehicleCategory getVehicleCategory() { return vehicleCategory; }
    public void setVehicleCategory(VehicleCategory vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Double getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(Double estimatedFare) { this.estimatedFare = estimatedFare; }
}