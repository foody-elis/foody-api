package com.example.foody.repository;

import com.example.foody.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // todo implement custom query to find restaurant by name (ignore case), street, ecc (endpoint: /search)
    List<Restaurant> findAllByDeletedAtIsNull();
    Optional<Restaurant> findByIdAndDeletedAtIsNull(long id);
}
