package com.platform.service.impl;

import com.platform.model.enums.DriverStatus;
import com.platform.model.enums.TripStatus;
import com.platform.model.enums.VerificationStatus;
import com.platform.model.support.Document;
import com.platform.model.user.Driver;
import com.platform.model.user.User;
import com.platform.repository.DocumentRepository;
import com.platform.repository.DriverRepository;
import com.platform.repository.TripRepository;
import com.platform.repository.UserRepository;
import com.platform.service.AdminService;
import com.platform.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final TripRepository tripRepository;
    private final PaymentService paymentService;

    @Autowired
    public AdminServiceImpl(DriverRepository driverRepository,
                             UserRepository userRepository,
                             DocumentRepository documentRepository,
                             TripRepository tripRepository,
                             PaymentService paymentService) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.tripRepository = tripRepository;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public void approveDriver(Long driverId) {
        Driver driver = getDriver(driverId);

        // Check all documents are submitted
        List<Document> docs = documentRepository.findByDriver(driver);
        if (docs.isEmpty()) {
            throw new IllegalStateException("Driver has not uploaded any documents.");
        }

        // Approve all pending documents
        docs.forEach(doc -> {
            if (doc.getVerificationStatus() == VerificationStatus.PENDING) {
                doc.approve();
            }
        });
        documentRepository.saveAll(docs);

        driver.setDriverStatus(DriverStatus.OFF_DUTY); // ready to go online
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void rejectDriver(Long driverId, String reason) {
        Driver driver = getDriver(driverId);

        List<Document> docs = documentRepository.findByDriver(driver);
        docs.forEach(doc -> doc.reject());
        documentRepository.saveAll(docs);

        // Keep status as PENDING_APPROVAL — driver can resubmit docs
        System.out.println("[ADMIN] Driver " + driverId + " rejected. Reason: " + reason);
    }

    @Override
    @Transactional
    public void suspendUser(Long userId, boolean lifetimeBan) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        user.setActive(false);
        userRepository.save(user);
        System.out.println("[ADMIN] User " + userId + " suspended. Lifetime ban: " + lifetimeBan);
    }

    @Override
    @Transactional
    public void reinstateDriver(Long driverId) {
        Driver driver = getDriver(driverId);
        driver.setActive(true);
        driver.setDriverStatus(DriverStatus.OFF_DUTY);
        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void issueRefund(Long invoiceId, String reason) {
        boolean success = paymentService.issueRefund(invoiceId);
        if (!success) {
            throw new IllegalStateException("Refund failed for invoice: " + invoiceId);
        }
        System.out.println("[ADMIN] Refund issued for invoice " + invoiceId + ". Reason: " + reason);
    }

    @Override
    public List<Driver> getPendingDriverApprovals() {
        return driverRepository.findByDriverStatus(DriverStatus.PENDING_APPROVAL);
    }

    @Override
    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers",        userRepository.count());
        stats.put("totalDrivers",      driverRepository.count());
        stats.put("activeTrips",       tripRepository.findByTripStatus(TripStatus.IN_PROGRESS).size());
        stats.put("completedTrips",    tripRepository.findByTripStatus(TripStatus.COMPLETED).size());
        stats.put("cancelledTrips",    tripRepository.findByTripStatus(TripStatus.CANCELLED).size());
        stats.put("pendingApprovals",  getPendingDriverApprovals().size());
        return stats;
    }

    private Driver getDriver(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
    }
}