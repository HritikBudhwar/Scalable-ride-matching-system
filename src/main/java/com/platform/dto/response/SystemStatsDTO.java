package com.platform.dto.response;

public class SystemStatsDTO {
    private long totalUsers;
    private long totalDrivers;
    private long activeTrips;
    private long completedTrips;
    private long cancelledTrips;
    private long pendingApprovals;

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalDrivers() { return totalDrivers; }
    public void setTotalDrivers(long totalDrivers) { this.totalDrivers = totalDrivers; }

    public long getActiveTrips() { return activeTrips; }
    public void setActiveTrips(long activeTrips) { this.activeTrips = activeTrips; }

    public long getCompletedTrips() { return completedTrips; }
    public void setCompletedTrips(long completedTrips) { this.completedTrips = completedTrips; }

    public long getCancelledTrips() { return cancelledTrips; }
    public void setCancelledTrips(long cancelledTrips) { this.cancelledTrips = cancelledTrips; }

    public long getPendingApprovals() { return pendingApprovals; }
    public void setPendingApprovals(long pendingApprovals) { this.pendingApprovals = pendingApprovals; }
}