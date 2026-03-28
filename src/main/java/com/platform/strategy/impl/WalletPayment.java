package com.platform.strategy.impl;

import com.platform.model.payment.Invoice;
import com.platform.strategy.PaymentStrategy;

/**
 * Wallet payment implementation
 */
public class WalletPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(Invoice invoice) {
        return false;
    }

    @Override
    public boolean refundPayment(Invoice invoice) {
        return false;
    }
}