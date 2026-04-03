package com.platform.dto.response;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for system statistics
 */
public class SystemStatsDTO {
    private Long totalUsers;
    private Long activeUsers;
    private Long totalDrivers;
    private Long activeDrivers;
    private Long totalCustomers;
    private Long activeCustomers;
    private Long totalTrips;
    private Long activeTrips;
    private Long completedTrips;
    private Long cancelledTrips;
    private Double totalRevenue;
    private Double todayRevenue;
    private Double monthlyRevenue;
    private Long pendingPayments;
    private Long completedPayments;
    private Long supportTickets;
    private Long resolvedTickets;
    private Long pendingVerifications;
    private Long emergencyReports;
    private LocalDateTime generatedAt;
    private String peakHours;
    private Double averageTripDuration;
    private Double averageFare;
    
    public SystemStatsDTO() {
        // Default constructor
    }
    
    public SystemStatsDTO(Long totalUsers, Long activeUsers, Long totalDrivers, Long totalCustomers, 
                        Long totalTrips, Double totalRevenue, Long supportTickets) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.totalDrivers = totalDrivers;
        this.totalCustomers = totalCustomers;
        this.totalTrips = totalTrips;
        this.totalRevenue = totalRevenue;
        this.supportTickets = supportTickets;
    }
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public Long getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(Long activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    public Long getTotalDrivers() {
        return totalDrivers;
    }
    
    public void setTotalDrivers(Long totalDrivers) {
        this.totalDrivers = totalDrivers;
    }
    
    public Long getActiveDrivers() {
        return activeDrivers;
    }
    
    public void setActiveDrivers(Long activeDrivers) {
        this.activeDrivers = activeDrivers;
    }
    
    public Long getTotalCustomers() {
        return totalCustomers;
    }
    
    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
    
    public Long getActiveCustomers() {
        return activeCustomers;
    }
    
    public void setActiveCustomers(Long activeCustomers) {
        this.activeCustomers = activeCustomers;
    }
    
    public Long getTotalTrips() {
        return totalTrips;
    }
    
    public void setTotalTrips(Long totalTrips) {
        this.totalTrips = totalTrips;
    }
    
    public Long getActiveTrips() {
        return activeTrips;
    }
    
    public void setActiveTrips(Long activeTrips) {
        this.activeTrips = activeTrips;
    }
    
    public Long getCompletedTrips() {
        return completedTrips;
    }
    
    public void setCompletedTrips(Long completedTrips) {
        this.completedTrips = completedTrips;
    }
    
    public Long getCancelledTrips() {
        return cancelledTrips;
    }
    
    public void setCancelledTrips(Long cancelledTrips) {
        this.cancelledTrips = cancelledTrips;
    }
    
    public Double getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public Double getTodayRevenue() {
        return todayRevenue;
    }
    
    public void setTodayRevenue(Double todayRevenue) {
        this.todayRevenue = todayRevenue;
    }
    
    public Double getMonthlyRevenue() {
        return monthlyRevenue;
    }
    
    public void setMonthlyRevenue(Double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
    
    public Long getPendingPayments() {
        return pendingPayments;
    }
    
    public void setPendingPayments(Long pendingPayments) {
        this.pendingPayments = pendingPayments;
    }
    
    public Long getCompletedPayments() {
        return completedPayments;
    }
    
    public void setCompletedPayments(Long completedPayments) {
        this.completedPayments = completedPayments;
    }
    
    public Long getSupportTickets() {
        return supportTickets;
    }
    
    public void setSupportTickets(Long supportTickets) {
        this.supportTickets = supportTickets;
    }
    
    public Long getResolvedTickets() {
        return resolvedTickets;
    }
    
    public void setResolvedTickets(Long resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }
    
    public Long getPendingVerifications() {
        return pendingVerifications;
    }
    
    public void setPendingVerifications(Long pendingVerifications) {
        this.pendingVerifications = pendingVerifications;
    }
    
    public Long getEmergencyReports() {
        return emergencyReports;
    }
    
    public void setEmergencyReports(Long emergencyReports) {
        this.emergencyReports = emergencyReports;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    public String getPeakHours() {
        return peakHours;
    }
    
    public void setPeakHours(String peakHours) {
        this.peakHours = peakHours;
    }
    
    public Double getAverageTripDuration() {
        return averageTripDuration;
    }
    
    public void setAverageTripDuration(Double averageTripDuration) {
        this.averageTripDuration = averageTripDuration;
    }
    
    public Double getAverageFare() {
        return averageFare;
    }
    
    public void setAverageFare(Double averageFare) {
        this.averageFare = averageFare;
    }
}
