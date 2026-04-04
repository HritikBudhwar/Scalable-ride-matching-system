package com.platform.model.payment;

import com.platform.model.ride.Trip;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing the final bill for a trip.
 * Links to the PricingEngine for fare calculation and PaymentService for settlement.
 */
@Data
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    // Fare breakdown fields used by PricingEngineImpl
    private Double baseFare;
    private Double distanceCharge;
    private Double timeCharge;
    private Double surgeMultiplier;
    private Double tax;
    private Double tip = 0.0;
    private Double totalAmount;

    // Status and audit fields used by PaymentServiceImpl
    private boolean paid = false;
    private String paymentMethod; // UPI | CASH | WALLET
    private String paymentRef;
    private LocalDateTime paidAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Business Logic Methods ---

    /**
     * Updates the invoice status to paid and records the timestamp.
     * Required by PaymentServiceImpl.processPayment().
     */
    public void processPayment() {
        this.paid = true;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * Resets the paid status.
     * Required by PaymentServiceImpl.issueRefund().
     */
    public void refund() {
        this.paid = false;
    }

    // --- Explicit Getters/Setters (to ensure compatibility with your services) ---

    public Long getId() { return id; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    public Double getBaseFare() { return baseFare; }
    public void setBaseFare(Double baseFare) { this.baseFare = baseFare; }

    public Double getDistanceCharge() { return distanceCharge; }
    public void setDistanceCharge(Double distanceCharge) { this.distanceCharge = distanceCharge; }

    public Double getTimeCharge() { return timeCharge; }
    public void setTimeCharge(Double timeCharge) { this.timeCharge = timeCharge; }

    public Double getSurgeMultiplier() { return surgeMultiplier; }
    public void setSurgeMultiplier(Double surgeMultiplier) { this.surgeMultiplier = surgeMultiplier; }

    public Double getTax() { return tax; }
    public void setTax(Double tax) { this.tax = tax; }

    public Double getTip() { return tip; }
    public void setTip(Double tip) { this.tip = tip; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void calculateTotal() {
    double subtotal = (this.baseFare + this.distanceCharge + this.timeCharge) * this.surgeMultiplier;
    double total = subtotal + this.tax + this.tip;
    
    // Round to 2 decimal places
    this.totalAmount = Math.round(total * 100.0) / 100.0;
}
}