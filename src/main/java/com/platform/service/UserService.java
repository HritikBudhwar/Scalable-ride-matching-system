package com.platform.service;

import com.platform.dto.request.UserRegistrationDTO;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.model.user.User;

/**
 * Handles registration, login, and profile management for all user types.
 */
public interface UserService {

    Customer registerCustomer(UserRegistrationDTO dto);

    Driver registerDriver(UserRegistrationDTO dto);

    /** Initiates OTP-based login — returns a session token on success. */
    String login(String phone, String otp);

    /** Sends OTP to the given phone number for login/verification. */
    void sendLoginOTP(String phone);

    User getUserById(Long id);

    User getUserByPhone(String phone);

    /** Deactivates a user account (admin action or self-delete). */
    void deactivateUser(Long userId);

    /** Updates wallet balance — used after payment or refund. */
    void updateWalletBalance(Long userId, double delta);
}