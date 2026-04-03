package com.platform.dto.response;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for payment responses
 */
public class PaymentResponseDTO {
    private Long id;
    private Long invoiceId;
    private String paymentMethod;
    private Double amount;
    private String transactionId;
    private String status; // SUCCESS, FAILED, PENDING, REFUNDED
    private String currency;
    private LocalDateTime processedAt;
    private LocalDateTime refundedAt;
    private Double processingFee;
    private Double netAmount;
    private String gatewayResponse;
    private String errorMessage;
    private String refundReason;
    private String paymentGateway;
    private String maskedCardNumber;
    private String walletId;
    private String upiId;
    
    public PaymentResponseDTO() {
        // Default constructor
    }
    
    public PaymentResponseDTO(Long id, Long invoiceId, String paymentMethod, 
                          Double amount, String transactionId, String status) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionId = transactionId;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }
    
    public void setRefundedAt(LocalDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }
    
    public Double getProcessingFee() {
        return processingFee;
    }
    
    public void setProcessingFee(Double processingFee) {
        this.processingFee = processingFee;
    }
    
    public Double getNetAmount() {
        return netAmount;
    }
    
    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }
    
    public String getGatewayResponse() {
        return gatewayResponse;
    }
    
    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getRefundReason() {
        return refundReason;
    }
    
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
    
    public String getPaymentGateway() {
        return paymentGateway;
    }
    
    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    
    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }
    
    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }
    
    public String getWalletId() {
        return walletId;
    }
    
    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
    
    public String getUpiId() {
        return upiId;
    }
    
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}
