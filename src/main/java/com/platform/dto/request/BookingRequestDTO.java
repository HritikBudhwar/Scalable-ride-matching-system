package com.platform.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.platform.model.enums.ServiceType;
import com.platform.model.enums.VehicleCategory;

public class BookingRequestDTO {

    @NotBlank
    private String source;       // "lat,lng"

    @NotBlank
    private String destination;  // "lat,lng"

    private String stopPoints;   // optional, comma-separated "lat,lng"

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private VehicleCategory vehicleCategory;

    private LocalDateTime scheduledTime; // null = book now

    // --- Getters and Setters ---
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getStopPoints() { return stopPoints; }
    public void setStopPoints(String stopPoints) { this.stopPoints = stopPoints; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public VehicleCategory getVehicleCategory() { return vehicleCategory; }
    public void setVehicleCategory(VehicleCategory vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
}