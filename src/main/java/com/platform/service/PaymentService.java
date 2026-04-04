package com.platform.service;

import com.platform.model.payment.Invoice;

/**
 * Handles payment processing using the Strategy pattern.
 * Design Pattern: Strategy — PaymentService selects the correct
 * PaymentStrategy (UPI/Cash/Wallet) at runtime.
 */
public interface PaymentService {

    /** Processes payment for a completed trip invoice. */
    Invoice processPayment(Long invoiceId, String paymentMethod);

    /** Issues a refund for a cancelled trip. */
    boolean issueRefund(Long invoiceId);

    /** Adds a tip to an existing paid invoice. */
    Invoice addTip(Long invoiceId, double tipAmount);

    /** Calculates and records driver payout after commission split. */
    void settleDriverPayout(Long invoiceId);

    Invoice getInvoiceByTripId(Long tripId);
}