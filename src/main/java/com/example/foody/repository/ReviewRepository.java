package com.example.foody.repository;

import com.example.foody.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByOrderByCreatedAtDesc();

    List<Review> findAllByCustomer_IdOrderByCreatedAtDesc(long customerId);

    List<Review> findAllByRestaurant_IdOrderByCreatedAtDesc(long restaurantId);

    List<Review> findAllByDish_IdOrderByCreatedAtDesc(long dishId);

    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.restaurant.id = :restaurantId
            """)
    double findAverageRatingByRestaurant_Id(long restaurantId);

    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.dish.id = :dishId
            """)
    double findAverageRatingByDish_Id(long dishId);

    @Query("""
            SELECT r
            FROM Review r
            WHERE r.restaurant.id = :restaurantId
            ORDER BY r.createdAt DESC
            LIMIT :limit
            """)
    List<Review> findAllByRestaurant_IdOrderByCreated_AtDescLimit(long restaurantId, int limit);
}