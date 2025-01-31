package com.example.foody.repository;

import com.example.foody.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link Review} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link Review} entities.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Finds all reviews ordered by creation date in descending order.
     *
     * @return a list of reviews ordered by creation date in descending order
     */
    List<Review> findAllByOrderByCreatedAtDesc();

    /**
     * Finds all reviews by the customer ID, ordered by creation date in descending order.
     *
     * @param customerId the ID of the customer
     * @return a list of reviews associated with the specified customer, ordered by creation date in descending order
     */
    List<Review> findAllByCustomer_IdOrderByCreatedAtDesc(long customerId);

    /**
     * Finds all reviews by the restaurant ID, ordered by creation date in descending order.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of reviews associated with the specified restaurant, ordered by creation date in descending order
     */
    List<Review> findAllByRestaurant_IdOrderByCreatedAtDesc(long restaurantId);

    /**
     * Finds all reviews by the dish ID, ordered by creation date in descending order.
     *
     * @param dishId the ID of the dish
     * @return a list of reviews associated with the specified dish, ordered by creation date in descending order
     */
    List<Review> findAllByDish_IdOrderByCreatedAtDesc(long dishId);

    /**
     * Finds the average rating for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the average rating of the specified restaurant
     */
    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.restaurant.id = :restaurantId
            """)
    double findAverageRatingByRestaurant_Id(long restaurantId);

    /**
     * Finds the average rating for a specific dish.
     *
     * @param dishId the ID of the dish
     * @return the average rating of the specified dish
     */
    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.dish.id = :dishId
            """)
    double findAverageRatingByDish_Id(long dishId);

    /**
     * Finds all reviews by the restaurant ID, ordered by creation date in descending order, limited to a specified number of results.
     *
     * @param restaurantId the ID of the restaurant
     * @param limit the maximum number of results to return
     * @return a list of reviews associated with the specified restaurant, ordered by creation date in descending order
     */
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.restaurant.id = :restaurantId
            ORDER BY r.createdAt DESC
            LIMIT :limit
            """)
    List<Review> findAllByRestaurant_IdOrderByCreated_AtDescLimit(long restaurantId, int limit);
}