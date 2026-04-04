package com.platform.strategy.payment;

public interface PaymentStrategy {
    
    boolean processPayment(double amount);

    // Added this so PaymentServiceImpl can identify the method used
    String getPaymentMethodName();

    // Added this to support the refund logic in your implementations
    boolean refund(Double amount);
}