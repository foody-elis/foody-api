package com.example.foody.repository;

import com.example.foody.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByApproved(boolean approved);

    Optional<Restaurant> findByIdAndApproved(long id, boolean approved);

    Optional<Restaurant> findAllByRestaurateur_Id(long restaurateurId);

    @Query("""
            SELECT r
            FROM Restaurant r
            JOIN r.categories c
            WHERE c.id = :categoryId
            """)
    List<Restaurant> findAllByCategory_Id(long categoryId);

    @Query("""
            SELECT r
            FROM Restaurant r
            JOIN r.categories c
            WHERE c.id = :categoryId
            AND r.approved = :approved
            """)
    List<Restaurant> findAllByCategory_IdAndApproved(long categoryId, boolean approved);

    boolean existsByRestaurateur_Id(long restaurateurId);
}