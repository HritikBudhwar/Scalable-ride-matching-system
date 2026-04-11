package com.platform.service.impl;

import com.platform.dto.request.UserRegistrationDTO;
import com.platform.model.enums.DriverStatus;
import com.platform.model.user.Administrator;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.model.user.User;
import com.platform.repository.AdministratorRepository;
import com.platform.repository.CustomerRepository;
import com.platform.repository.DriverRepository;
import com.platform.repository.UserRepository;
import com.platform.service.OTPManager;
import com.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final AdministratorRepository administratorRepository;
    private final UserRepository userRepository;
    private final OTPManager otpManager;

    @Autowired
    public UserServiceImpl(CustomerRepository customerRepository,
                           DriverRepository driverRepository,
                           AdministratorRepository administratorRepository,
                           UserRepository userRepository,
                           OTPManager otpManager) {
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.administratorRepository = administratorRepository;
        this.userRepository = userRepository;
        this.otpManager = otpManager;
    }

    @Override
    @Transactional
    public Customer registerCustomer(UserRegistrationDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone already registered: " + dto.getPhone());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
        }

        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setPasswordHash(dto.getPassword()); // Production: BCrypt hash
        customer.setGender(dto.getGender());
        customer.setEmergencyContact(dto.getEmergencyContact());

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Driver registerDriver(UserRegistrationDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone already registered: " + dto.getPhone());
        }

        Driver driver = new Driver();
        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setEmail(dto.getEmail());
        driver.setPhone(dto.getPhone());
        driver.setPasswordHash(dto.getPassword());
        driver.setGender(dto.getGender());
        // Demo-friendly default: allow drivers to accept trips immediately.
        // (Admin approval flow exists separately but would otherwise block basic demo.)
        driver.setDriverStatus(DriverStatus.AVAILABLE);

        return driverRepository.save(driver);
    }

    @Override
    @Transactional
    public Administrator registerAdmin(UserRegistrationDTO dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone already registered: " + dto.getPhone());
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + dto.getEmail());
        }

        Administrator admin = new Administrator();
        admin.setFirstName(dto.getFirstName());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setPasswordHash(dto.getPassword());
        admin.setGender(dto.getGender());
        admin.setAdminId("ADMIN-" + System.currentTimeMillis());

        return administratorRepository.save(admin);
    }

    @Override
    public void sendLoginOTP(String phone) {
        userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("No user found with phone: " + phone));

        String context = "LOGIN:" + phone;
        String otp = otpManager.generateOTP(context);
        otpManager.sendOTP(phone, otp);
    }

    @Override
    public String login(String phone, String otp) {
        String context = "LOGIN:" + phone;
        if (!otpManager.validateOTP(context, otp)) {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }
        otpManager.invalidateOTP(context);

        // Production: generate JWT token here
        return "SESSION_TOKEN_" + phone + "_" + System.currentTimeMillis();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + phone));
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateWalletBalance(Long userId, double delta) {
        User user = getUserById(userId);
        double newBalance = user.getWalletBalance() + delta;
        if (newBalance < 0) {
            throw new IllegalStateException("Insufficient wallet balance.");
        }
        user.setWalletBalance(newBalance);
        userRepository.save(user);
    }
}