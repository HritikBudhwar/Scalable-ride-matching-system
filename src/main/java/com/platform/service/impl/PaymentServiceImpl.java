package com.platform.service.impl;

import com.platform.model.enums.EarningSource;
import com.platform.model.payment.DriverPayoutModel;
import com.platform.model.payment.Invoice;
import com.platform.model.user.Driver;
import com.platform.repository.DriverRepository;
import com.platform.repository.InvoiceRepository;
import com.platform.service.PaymentService;
import com.platform.strategy.payment.PaymentStrategy;
import com.platform.strategy.payment.impl.CashPayment;
import com.platform.strategy.payment.impl.UPIPayment;
import com.platform.strategy.payment.impl.WalletPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Design Pattern: Strategy (Behavioral)
 * selectStrategy() picks the PaymentStrategy at runtime based on
 * the payment method string — satisfies OCP (new methods = new class, no edits here).
 *
 * Design Pattern: Also demonstrates revenue splitting (synopsis 6.2).
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final double PLATFORM_COMMISSION_RATE = 0.20; // 20%

    private final InvoiceRepository invoiceRepository;
    private final DriverRepository driverRepository;
    private final UPIPayment upiPayment;
    private final CashPayment cashPayment;
    private final WalletPayment walletPayment;

    @Autowired
    public PaymentServiceImpl(InvoiceRepository invoiceRepository,
                               DriverRepository driverRepository,
                               UPIPayment upiPayment,
                               CashPayment cashPayment,
                               WalletPayment walletPayment) {
        this.invoiceRepository = invoiceRepository;
        this.driverRepository = driverRepository;
        this.upiPayment = upiPayment;
        this.cashPayment = cashPayment;
        this.walletPayment = walletPayment;
    }

    @Override
    @Transactional
    public Invoice processPayment(Long invoiceId, String paymentMethod) {
        Invoice invoice = getInvoice(invoiceId);

        if (invoice.isPaid()) {
            throw new IllegalStateException("Invoice already paid.");
        }

        PaymentStrategy strategy = selectStrategy(paymentMethod);
        boolean success = strategy.processPayment(invoice.getTotalAmount());

        if (!success) {
            throw new IllegalStateException("Payment failed. Please try again.");
        }

        invoice.processPayment();
        invoice.setPaymentMethod(strategy.getPaymentMethodName());

        // Deduct from wallet if wallet payment
        if ("WALLET".equals(paymentMethod)) {
            Long customerId = invoice.getTrip().getCustomer().getId();
            double balance = invoice.getTrip().getCustomer().getWalletBalance();
            if (balance < invoice.getTotalAmount()) {
                throw new IllegalStateException("Insufficient wallet balance.");
            }
            invoice.getTrip().getCustomer().setWalletBalance(
                balance - invoice.getTotalAmount());
        }

        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public boolean issueRefund(Long invoiceId) {
        Invoice invoice = getInvoice(invoiceId);

        if (!invoice.isPaid()) {
            throw new IllegalStateException("Invoice is not paid — nothing to refund.");
        }

        PaymentStrategy strategy = selectStrategy(invoice.getPaymentMethod());
        boolean success = strategy.refund(invoice.getTotalAmount());

        if (success) {
            invoice.refund();
            invoiceRepository.save(invoice);
        }

        return success;
    }

    @Override
    @Transactional
    public Invoice addTip(Long invoiceId, double tipAmount) {
        Invoice invoice = getInvoice(invoiceId);

        if (tipAmount <= 0) {
            throw new IllegalArgumentException("Tip amount must be positive.");
        }

        invoice.setTip(invoice.getTip() + tipAmount);
        invoice.calculateTotal();

        // Credit tip directly to driver wallet
        Driver driver = invoice.getTrip().getDriver();
        if (driver != null) {
            driver.setWalletBalance(driver.getWalletBalance() + tipAmount);
            driver.setTotalEarnings(driver.getTotalEarnings() + tipAmount);
            driverRepository.save(driver);
        }

        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public void settleDriverPayout(Long invoiceId) {
        Invoice invoice = getInvoice(invoiceId);
        Driver driver = invoice.getTrip().getDriver();

        if (driver == null) return;

        // Revenue split: 80% to driver, 20% platform
        DriverPayoutModel payout = new DriverPayoutModel();
        payout.setDriver(driver);
        payout.setAmount(invoice.getTotalAmount());
        payout.setSource(EarningSource.RIDE);
        payout.split(PLATFORM_COMMISSION_RATE);
        payout.setPayoutStatus("PENDING");

        // Credit driver's wallet with their share
        driver.setWalletBalance(driver.getWalletBalance() + payout.getDriverShare());
        driver.setTotalEarnings(driver.getTotalEarnings() + payout.getDriverShare());
        driverRepository.save(driver);
    }

    @Override
    public Invoice getInvoiceByTripId(Long tripId) {
        return invoiceRepository.findByTripId(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found for trip: " + tripId));
    }

    // ---- Private helpers ----

    /**
     * Strategy selection at runtime based on payment method string.
     * Design Pattern: Strategy — context selects concrete strategy.
     */
    private PaymentStrategy selectStrategy(String method) {
        if (method == null) throw new IllegalArgumentException("Payment method cannot be null.");
        switch (method.toUpperCase()) {
            case "UPI":    return upiPayment;
            case "CASH":   return cashPayment;
            case "WALLET": return walletPayment;
            default: throw new IllegalArgumentException("Unknown payment method: " + method);
        }
    }

    private Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));
    }
}