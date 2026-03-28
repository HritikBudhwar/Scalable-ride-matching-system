package com.platform.strategy.impl;

import com.platform.model.payment.Invoice;
import com.platform.strategy.PaymentStrategy;

/**
 * UPI payment implementation
 */
public class UPIPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(Invoice invoice) {
        return false;
    }

    @Override
    public boolean refundPayment(Invoice invoice) {
        return false;
    }
}