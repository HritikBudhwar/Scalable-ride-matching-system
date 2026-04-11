package com.platform.strategy.payment.impl;

import com.platform.strategy.payment.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class CashPayment implements PaymentStrategy {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Cash Payment: Driver to collect ₹" + amount + " from the rider.");
        return true; // Return true to indicate the "transaction" is initiated
    }

    @Override
    public String getPaymentMethodName() {
        return "CASH";
    }

    @Override
    public boolean refund(Double amount) {
        // In a real app, you might log this for manual admin intervention
        System.out.println("Cash Refund: Manual refund of ₹" + amount + " required (Cash transaction).");
        return true;
    }
}