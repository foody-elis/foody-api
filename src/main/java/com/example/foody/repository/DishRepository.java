package com.example.foody.repository;

import com.example.foody.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByRestaurant_Id(long restaurantId);

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