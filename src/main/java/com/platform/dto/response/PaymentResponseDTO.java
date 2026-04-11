package com.platform.dto.response;

public class PaymentResponseDTO {
    private Long invoiceId;
    private Double totalAmount;
    private String paymentMethod;
    private boolean paid;
    private String message;

    // Getters and Setters
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}