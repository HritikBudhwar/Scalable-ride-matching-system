package com.platform.controller;

import com.platform.dto.request.UserRegistrationDTO;
import com.platform.model.user.Administrator;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.model.user.User;
import com.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> registerCustomer(@RequestBody UserRegistrationDTO dto) {
        Customer customer = userService.registerCustomer(dto);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PostMapping("/driver")
    public ResponseEntity<Driver> registerDriver(@RequestBody UserRegistrationDTO dto) {
        Driver driver = userService.registerDriver(dto);
        return new ResponseEntity<>(driver, HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<Administrator> registerAdmin(@RequestBody UserRegistrationDTO dto) {
        Administrator admin = userService.registerAdmin(dto);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @PostMapping("/login/otp")
    public ResponseEntity<Void> sendLoginOTP(@RequestParam String phone) {
        userService.sendLoginOTP(phone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String phone, @RequestParam String otp) {
        String token = userService.login(phone, otp);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}