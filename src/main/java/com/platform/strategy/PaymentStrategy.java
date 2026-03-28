package com.platform.strategy;

import com.platform.model.payment.Invoice;

/**
 * Interface for payment processing strategies
 */
public interface PaymentStrategy {
    boolean processPayment(Invoice invoice);
    boolean refundPayment(Invoice invoice);
}