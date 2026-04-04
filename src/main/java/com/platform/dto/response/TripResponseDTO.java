package com.platform.dto.response;

import com.platform.model.enums.TripStatus;
import java.time.LocalDateTime;

public class TripResponseDTO {
    private Long id;
    private String source;
    private String destination;
    private TripStatus status;
    private String driverName;
    private String vehicleReg;
    private String vehicleModel;
    private Double distanceTraveled;
    private Double invoiceTotal;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Getters and Setters (Standard boilerplate)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getVehicleReg() { return vehicleReg; }
    public void setVehicleReg(String vehicleReg) { this.vehicleReg = vehicleReg; }
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    public Double getDistanceTraveled() { return distanceTraveled; }
    public void setDistanceTraveled(Double distanceTraveled) { this.distanceTraveled = distanceTraveled; }
    public Double getInvoiceTotal() { return invoiceTotal; }
    public void setInvoiceTotal(Double invoiceTotal) { this.invoiceTotal = invoiceTotal; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}