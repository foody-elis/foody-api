package com.example.foody.repository;

import com.example.foody.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.deletedAt IS NULL AND r.restaurant.id = :restaurantId
            """)
    double findAverageRatingByRestaurant_Id(long restaurantId);

    @Query("""
            SELECT r
            FROM Review r
            WHERE r.deletedAt IS NULL AND r.restaurant.id = :restaurantId
            ORDER BY r.createdAt DESC
            LIMIT :limit
            """)
    List<Review> findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreated_AtDescLimit(long restaurantId, int limit);
}