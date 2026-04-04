package com.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ride-Hailing & Logistics Platform
 * Team E11 — OOAD Mini Project
 *
 * Runs on: http://localhost:8080
 * H2 Console: http://localhost:8080/h2-console
 *   JDBC URL: jdbc:h2:mem:testdb
 *   Username: sa  |  Password: (blank)
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}