package com.platform.service;

import com.platform.dto.request.BookingRequestDTO;
import com.platform.model.ride.BookingRequest;
import com.platform.model.ride.Trip;
import java.util.List;

public interface BookingService {
    BookingRequest createBooking(BookingRequestDTO dto, Long customerId);
    Trip confirmBooking(Long bookingRequestId);
    void cancelBooking(Long bookingRequestId, String reason);
    List<BookingRequest> getCustomerBookings(Long customerId);
    BookingRequest getBookingById(Long id);
}