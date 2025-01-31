package com.example.foody.repository;

import com.example.foody.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Restaurant entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /**
     * Finds all restaurants by their approval status.
     *
     * @param approved the approval status of the restaurants
     * @return a list of restaurants with the specified approval status
     */
    List<Restaurant> findAllByApproved(boolean approved);

    /**2
     * Finds a restaurant by its ID and approval status.
     *
     * @param id the ID of the restaurant
     * @param approved the approval status of the restaurant
     * @return an Optional containing the restaurant if found, or empty if not found
     */
    Optional<Restaurant> findByIdAndApproved(long id, boolean approved);

    /**
     * Finds a restaurant by the ID of its restaurateur.
     *
     * @param restaurateurId the ID of the restaurateur
     * @return an Optional containing the restaurant if found, or empty if not found
     */
    Optional<Restaurant> findByRestaurateur_Id(long restaurateurId);

    /**
     * Finds all restaurants by the ID of a category.
     *
     * @param categoryId the ID of the category
     * @return a list of restaurants belonging to the specified category
     */
    List<Restaurant> findAllByCategories_Id(long categoryId);

    /**
     * Finds all restaurants by the ID of a category and their approval status.
     *
     * @param categoryId the ID of the category
     * @param approved the approval status of the restaurants
     * @return a list of restaurants belonging to the specified category and with the specified approval status
     */
    List<Restaurant> findAllByCategories_IdAndApproved(long categoryId, boolean approved);

    /**
     * Checks if a restaurant exists by the ID of its restaurateur.
     *
     * @param restaurateurId the ID of the restaurateur
     * @return true if a restaurant exists with the specified restaurateur ID, false otherwise
     */
    boolean existsByRestaurateur_Id(long restaurateurId);
}