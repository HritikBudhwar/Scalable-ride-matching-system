package com.platform.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTPManager {

    private static final int DEFAULT_EXPIRY_SECONDS = 300;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, OTPRecord> otpStore = new ConcurrentHashMap<>();
    private int expirySeconds = DEFAULT_EXPIRY_SECONDS;

    public String generateOTP(String context) {
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        long expiresAtEpochSeconds = Instant.now().getEpochSecond() + expirySeconds;
        otpStore.put(context, new OTPRecord(otp, expiresAtEpochSeconds));
        return otp;
    }

    public void sendOTP(String recipient, String otp) {
        // Placeholder for SMS/notification integration.
    }

    public Boolean validateOTP(String context, String input) {
        OTPRecord otpRecord = otpStore.get(context);
        if (otpRecord == null) {
            return false;
        }
        long nowEpochSeconds = Instant.now().getEpochSecond();
        if (nowEpochSeconds > otpRecord.getExpiresAtEpochSeconds()) {
            otpStore.remove(context);
            return false;
        }
        return otpRecord.getOtp().equals(input);
    }

    public void invalidateOTP(String context) {
        otpStore.remove(context);
    }

    public String getOTP(String context) {
        OTPRecord otpRecord = otpStore.get(context);
        if (otpRecord == null) {
            return null;
        }
        long nowEpochSeconds = Instant.now().getEpochSecond();
        if (nowEpochSeconds > otpRecord.getExpiresAtEpochSeconds()) {
            otpStore.remove(context);
            return null;
        }
        return otpRecord.getOtp();
    }

    private static final class OTPRecord {
        private final String otp;
        private final long expiresAtEpochSeconds;

        private OTPRecord(String otp, long expiresAtEpochSeconds) {
            this.otp = otp;
            this.expiresAtEpochSeconds = expiresAtEpochSeconds;
        }

        private String getOtp() {
            return otp;
        }

        private long getExpiresAtEpochSeconds() {
            return expiresAtEpochSeconds;
        }
    }
}