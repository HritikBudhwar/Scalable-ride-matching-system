package com.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.platform.model.ride.Trip;
import com.platform.model.support.Message;
import com.platform.model.user.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByTripOrderByTimestampAsc(Trip trip);
    List<Message> findByReceiverAndReadFalse(User receiver);
    List<Message> findBySenderAndReceiver(User sender, User receiver);
}