package com.platform.model.ride;

import com.platform.model.enums.VehicleCategory;

/**
 * Vehicle entity class
 */
public class Vehicle {
    private Long id;
    private String make;
    private String model;
    private String licensePlate;
    private VehicleCategory category;
    private int year;
    private String color;
}