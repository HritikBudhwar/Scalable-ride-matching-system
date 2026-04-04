package com.platform.model.payment;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.platform.model.enums.EarningSource;
import com.platform.model.user.Driver;

/**
 * Tracks individual earning entries for a Driver.
 * Supports revenue splitting: platform commission vs driver payout.
 */
@Entity
@Table(name = "driver_payouts")
public class DriverPayoutModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private EarningSource source;   // RIDE | PARCEL | TIP

    private Double platformCommission;   // amount retained by platform
    private Double driverShare;          // amount credited to driver

    // PENDING_APPROVAL → AVAILABLE → PAID
    private String payoutStatus = "PENDING";

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    /**
     * Splits the earning: default 80% to driver, 20% platform commission.
     */
    public void split(double commissionRate) {
        this.platformCommission = amount * commissionRate;
        this.driverShare = amount - platformCommission;
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public EarningSource getSource() { return source; }
    public void setSource(EarningSource source) { this.source = source; }

    public Double getPlatformCommission() { return platformCommission; }
    public Double getDriverShare() { return driverShare; }

    public String getPayoutStatus() { return payoutStatus; }
    public void setPayoutStatus(String payoutStatus) { this.payoutStatus = payoutStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}