package com.platform.service;

import org.springframework.stereotype.Service;

@Service
public class OTPManager {

    private int expirySeconds = 300; 

    public String generateOTP(String context) {
        return "123456"; // Implementation placeholder
    }

    public void sendOTP(String recipient, String otp) {
        // Implementation here
    }

    public Boolean validateOTP(String context, String input) {
        return true; // Implementation placeholder
    }

    public void invalidateOTP(String context) {
        // Implementation here
    }
}