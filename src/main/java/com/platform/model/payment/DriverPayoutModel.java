package com.platform.model.payment;

import com.platform.model.enums.EarningSource;
import com.platform.model.user.Driver;

import java.time.LocalDateTime;

/**
 * Driver payout model entity class
 */
public class DriverPayoutModel {
    private Long id;
    private Driver driver;
    private EarningSource earningSource;
    private double amount;
    private LocalDateTime earnedAt;
    private LocalDateTime paidAt;
    private boolean isPaid;
}