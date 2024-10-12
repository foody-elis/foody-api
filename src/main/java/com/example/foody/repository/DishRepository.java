package com.example.foody.repository;

import com.example.foody.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByDeletedAtIsNull();

    Optional<Dish> findByIdAndDeletedAtIsNull(long id);

    @Query("select d from Dish d where d.deletedAt is null and d.restaurant.id = :restaurantId")
    List<Dish> findAllByDeletedAtIsNullAndRestaurant(long restaurantId);
}
