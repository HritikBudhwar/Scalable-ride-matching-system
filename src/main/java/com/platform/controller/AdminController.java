package com.platform.controller;

import com.platform.dto.request.AdminActionDTO;
import com.platform.dto.response.AdminResponseDTO;
import com.platform.dto.response.SystemStatsDTO;
import com.platform.model.user.Driver;
import com.platform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Admin governance endpoints: driver approvals, user management, stats.
 * MVC Pattern: Controller — thin layer delegating to AdminService.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * GET /api/admin/drivers/pending
     * Returns all drivers awaiting approval.
     */
    @GetMapping("/drivers/pending")
    public ResponseEntity<List<AdminResponseDTO>> getPendingDrivers() {
        List<AdminResponseDTO> result = adminService.getPendingDriverApprovals()
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/admin/drivers/{id}/approve
     */
    @PostMapping("/drivers/{id}/approve")
    public ResponseEntity<Map<String, String>> approveDriver(@PathVariable Long id) {
        adminService.approveDriver(id);
        return ResponseEntity.ok(Map.of("message", "Driver approved successfully."));
    }

    /**
     * POST /api/admin/drivers/{id}/reject
     * Body: { "reason": "Incomplete documents" }
     */
    @PostMapping("/drivers/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectDriver(
            @PathVariable Long id,
            @RequestBody AdminActionDTO dto) {
        adminService.rejectDriver(id, dto.getReason());
        return ResponseEntity.ok(Map.of("message", "Driver rejected."));
    }

    /**
     * POST /api/admin/users/{id}/suspend
     * Body: { "lifetimeBan": false }
     */
    @PostMapping("/users/{id}/suspend")
    public ResponseEntity<Map<String, String>> suspendUser(
            @PathVariable Long id,
            @RequestBody AdminActionDTO dto) {
        adminService.suspendUser(id, dto.isLifetimeBan());
        return ResponseEntity.ok(Map.of("message", "User suspended."));
    }

    /**
     * POST /api/admin/drivers/{id}/reinstate
     */
    @PostMapping("/drivers/{id}/reinstate")
    public ResponseEntity<Map<String, String>> reinstateDriver(@PathVariable Long id) {
        adminService.reinstateDriver(id);
        return ResponseEntity.ok(Map.of("message", "Driver reinstated."));
    }

    /**
     * POST /api/admin/invoices/{id}/refund
     * Body: { "reason": "Lost parcel" }
     */
    @PostMapping("/invoices/{id}/refund")
    public ResponseEntity<Map<String, String>> issueRefund(
            @PathVariable Long id,
            @RequestBody AdminActionDTO dto) {
        adminService.issueRefund(id, dto.getReason());
        return ResponseEntity.ok(Map.of("message", "Refund issued."));
    }

    /**
     * GET /api/admin/stats
     * Returns system-wide analytics dashboard data.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(adminService.getSystemStats());
    }

    // ---- Mapper ----
    private AdminResponseDTO toDTO(Driver driver) {
        AdminResponseDTO dto = new AdminResponseDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getFirstName() + " " + driver.getLastName());
        dto.setEmail(driver.getEmail());
        dto.setPhone(driver.getPhone());
        dto.setStatus(driver.getDriverStatus().name());
        return dto;
    }
}