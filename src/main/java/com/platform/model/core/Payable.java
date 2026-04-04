package com.platform.model.core;

public interface Payable {
    boolean processPayment();
    boolean refund();
}