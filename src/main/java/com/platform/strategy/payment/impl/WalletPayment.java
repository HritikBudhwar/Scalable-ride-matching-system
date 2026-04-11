package com.platform.strategy.payment.impl;

import com.platform.strategy.payment.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class WalletPayment implements PaymentStrategy {

    // Removed the walletId field and constructors!

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Wallet Payment: Successfully deducted ₹" + amount + " from platform wallet.");
        return true;
    }

    @Override
    public String getPaymentMethodName() {
        return "WALLET";
    }

    @Override
    public boolean refund(Double amount) {
        System.out.println("Wallet Refund: Credited ₹" + amount + " back to platform wallet.");
        return true;
    }
}