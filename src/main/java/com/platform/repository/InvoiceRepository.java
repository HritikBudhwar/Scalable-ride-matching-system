package com.platform.repository;

import com.platform.model.payment.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Invoice entities
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByIsPaid(boolean isPaid);
    List<Invoice> findByTripId(Long tripId);
}