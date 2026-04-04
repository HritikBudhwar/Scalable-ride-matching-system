package com.platform.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PaymentRequestDTO {

    @NotNull
    private Long invoiceId;

    @NotBlank
    private String paymentMethod; // UPI | CASH | WALLET

    private Double tipAmount;     // optional

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Double getTipAmount() { return tipAmount; }
    public void setTipAmount(Double tipAmount) { this.tipAmount = tipAmount; }
}