package com.platform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.dto.request.PaymentRequestDTO;
import com.platform.dto.response.PaymentResponseDTO;
import com.platform.model.payment.Invoice;
import com.platform.service.PaymentService;

/**
 * Handles payment processing, refunds, tips, and driver payouts.
 * MVC Pattern: Controller — delegates to PaymentService which uses Strategy pattern.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Process payment for a completed trip.
     * POST /api/payments
     * Body: { "invoiceId": 1, "paymentMethod": "UPI" }
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @RequestBody PaymentRequestDTO dto) {
        Invoice invoice = paymentService.processPayment(dto.getInvoiceId(), dto.getPaymentMethod());
        return ResponseEntity.ok(toDTO(invoice, "Payment successful."));
    }

    /**
     * Get invoice for a trip.
     * GET /api/payments/invoice?tripId=3
     */
    @GetMapping("/invoice")
    public ResponseEntity<PaymentResponseDTO> getInvoice(@RequestParam Long tripId) {
        Invoice invoice = paymentService.getInvoiceByTripId(tripId);
        return ResponseEntity.ok(toDTO(invoice, "Invoice fetched."));
    }

    /**
     * Issue a refund for a cancelled trip.
     * POST /api/payments/{invoiceId}/refund
     */
    @PostMapping("/{invoiceId}/refund")
    public ResponseEntity<Map<String, String>> issueRefund(@PathVariable Long invoiceId) {
        boolean success = paymentService.issueRefund(invoiceId);
        String msg = success ? "Refund issued successfully." : "Refund failed.";
        return ResponseEntity.ok(Map.of("message", msg));
    }

    /**
     * Add a tip to a completed trip.
     * POST /api/payments/{invoiceId}/tip
     * Body: { "amount": 50.0 }
     */
    @PostMapping("/{invoiceId}/tip")
    public ResponseEntity<PaymentResponseDTO> addTip(
            @PathVariable Long invoiceId,
            @RequestBody Map<String, Double> body) {
        double tip = body.getOrDefault("amount", 0.0);
        Invoice invoice = paymentService.addTip(invoiceId, tip);
        return ResponseEntity.ok(toDTO(invoice, "Tip added successfully."));
    }

    /**
     * Settle driver payout after trip payment.
     * POST /api/payments/{invoiceId}/settle
     */
    @PostMapping("/{invoiceId}/settle")
    public ResponseEntity<Map<String, String>> settleDriverPayout(@PathVariable Long invoiceId) {
        paymentService.settleDriverPayout(invoiceId);
        return ResponseEntity.ok(Map.of("message", "Driver payout settled."));
    }

    // ---- Mapper ----
    private PaymentResponseDTO toDTO(Invoice invoice, String message) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setInvoiceId(invoice.getId());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setPaymentMethod(invoice.getPaymentMethod());
        dto.setPaid(invoice.isPaid());
        dto.setMessage(message);
        return dto;
    }
}