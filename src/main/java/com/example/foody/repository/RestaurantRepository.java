package com.example.foody.repository;

import com.example.foody.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByDeletedAtIsNull();

    List<Restaurant> findAllByDeletedAtIsNullAndApproved(boolean approved);

    Optional<Restaurant> findByIdAndDeletedAtIsNull(long id);

    Optional<Restaurant> findByIdAndDeletedAtIsNullAndApproved(long id, boolean approved);

    Optional<Restaurant> findAllByDeletedAtIsNullAndRestaurateur_Id(long restaurateurId);

    @Query("""
            SELECT r
            FROM Restaurant r
            JOIN r.categories c
            WHERE r.deletedAt IS NULL
            AND c.id = :categoryId
            """)
    List<Restaurant> findAllByDeletedAtIsNullAndCategory_Id(long categoryId);

    @Query("""
            SELECT r
            FROM Restaurant r
            JOIN r.categories c
            WHERE r.deletedAt IS NULL
            AND c.id = :categoryId
            AND r.approved = :approved
            """)
    List<Restaurant> findAllByDeletedAtIsNullAndCategory_IdAndApproved(long categoryId, boolean approved);

    boolean existsByDeletedAtIsNullAndRestaurateur_Id(long restaurateurId);

    // todo implement custom query to find restaurant by name (ignore case), street, ecc (endpoint: /search)
}
