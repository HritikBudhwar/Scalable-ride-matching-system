package com.platform.service;

import com.platform.model.user.Driver;
import com.platform.model.user.User;
import com.platform.model.ride.Trip;

import java.util.List;
import java.util.Map;

/**
 * All administrator governance operations.
 */
public interface AdminService {

    /** Reviews submitted documents and approves the driver. */
    void approveDriver(Long driverId);

    /** Rejects driver application with a reason. */
    void rejectDriver(Long driverId, String reason);

    /** Suspends a user or driver account. */
    void suspendUser(Long userId, boolean lifetimeBan);

    /** Reinstates a previously suspended driver. */
    void reinstateDriver(Long driverId);

    /** Issues a refund from admin side (e.g. dispute resolution). */
    void issueRefund(Long invoiceId, String reason);

    /** Returns pending driver approvals. */
    List<Driver> getPendingDriverApprovals();

    /** Returns all registered users. */
    List<User> getAllUsers();

    /** Returns all trips ordered by newest first. */
    List<Trip> getAllTrips();

    /**
     * Generates a summary stats map — active trips, revenue, cancellations.
     * Used for the admin analytics dashboard (synopsis section 7.2).
     */
    Map<String, Object> getSystemStats();
}