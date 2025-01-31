package com.example.foody.repository;

import com.example.foody.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link Dish} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link Dish} entities.
 */
public interface DishRepository extends JpaRepository<Dish, Long> {

    /**
     * Finds all dishes by the restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of dishes associated with the specified restaurant
     */
    List<Dish> findAllByRestaurant_Id(long restaurantId);

    /**
     * Finds all dishes by the restaurant ID, ordered by average rating in descending order, limited to a specified number of results.
     *
     * @param restaurantId the ID of the restaurant
     * @param limit the maximum number of results to return
     * @return a list of dishes associated with the specified restaurant, ordered by average rating in descending order
     */
    @Query("""
            SELECT d
            FROM Dish d
            LEFT JOIN d.reviews r ON d.id = r.dish.id
            WHERE d.restaurant.id = :restaurantId
            GROUP BY d.id
            ORDER BY COALESCE(AVG(r.rating), 0) DESC
            LIMIT :limit
            """)
    List<Dish> findAllByRestaurant_IdOrderByAverageRatingDescLimit(long restaurantId, int limit);
}