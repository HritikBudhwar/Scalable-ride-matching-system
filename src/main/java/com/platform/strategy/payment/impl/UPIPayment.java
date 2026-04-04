package com.platform.strategy.payment.impl;

import com.platform.strategy.payment.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class UPIPayment implements PaymentStrategy {

    // Removed the upiId field and constructors!

    @Override
    public boolean processPayment(double amount) {
        System.out.println("UPI Payment: Successfully processed ₹" + amount);
        return true; 
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI";
    }

    @Override
    public boolean refund(Double amount) {
        System.out.println("UPI Refund: Initiated refund of ₹" + amount + " to source account.");
        return true;
    }
}