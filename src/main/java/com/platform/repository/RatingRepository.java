package com.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.platform.model.ride.Trip;
import com.platform.model.support.Rating;
import com.platform.model.user.User;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByReviewee(User reviewee);
    List<Rating> findByReviewer(User reviewer);
    List<Rating> findByTrip(Trip trip);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.reviewee = :user")
    Double findAverageScoreForUser(User user);
}