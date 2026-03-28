package com.platform.model.support;

import com.platform.model.user.Customer;
import com.platform.model.user.Driver;

import java.time.LocalDateTime;

/**
 * Rating entity class
 */
public class Rating {
    private Long id;
    private Customer customer;
    private Driver driver;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}