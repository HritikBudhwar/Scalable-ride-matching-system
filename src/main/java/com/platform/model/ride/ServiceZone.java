package com.platform.model.ride;

import com.platform.model.enums.ServiceType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a geographic region where specific service types are active.
 * Managed exclusively by Administrators.
 */
@Entity
@Table(name = "service_zones")
public class ServiceZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String zoneName;

    // Polygon boundary stored as JSON array of "lat,lng" points
    @Column(length = 2000)
    private String boundary;

    private boolean active = true;

    @ElementCollection(targetClass = ServiceType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "zone_service_types",
                     joinColumns = @JoinColumn(name = "zone_id"))
    @Column(name = "service_type")
    private List<ServiceType> serviceTypes = new ArrayList<>();

    /**
     * Checks if a given lat/lng point falls within this zone.
     * Simplified bounding-box check for demo purposes.
     */
    public boolean containsPoint(String geoPoint) {
        // Production: use proper polygon contains check
        // For now, always returns true if zone is active
        return active;
    }

    public void addServiceType(ServiceType type) {
        if (!serviceTypes.contains(type)) serviceTypes.add(type);
    }

    public void removeServiceType(ServiceType type) {
        serviceTypes.remove(type);
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public String getBoundary() { return boundary; }
    public void setBoundary(String boundary) { this.boundary = boundary; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<ServiceType> getServiceTypes() { return serviceTypes; }
    public void setServiceTypes(List<ServiceType> serviceTypes) { this.serviceTypes = serviceTypes; }
}