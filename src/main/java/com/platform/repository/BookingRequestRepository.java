package com.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.platform.model.enums.TripStatus;
import com.platform.model.ride.BookingRequest;
import com.platform.model.user.Customer;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {
    List<BookingRequest> findByCustomer(Customer customer);
    List<BookingRequest> findByStatus(TripStatus status);
    List<BookingRequest> findByCustomerOrderByCreatedAtDesc(Customer customer);
}