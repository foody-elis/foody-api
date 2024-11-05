package com.example.foody.repository;

import com.example.foody.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByDeletedAtIsNull();

    @Query("select r from Restaurant r where r.deletedAt is null and r.approved = :approved")
    List<Restaurant> findAllByDeletedAtIsNullAndApproved(boolean approved);

    Optional<Restaurant> findByIdAndDeletedAtIsNull(long id);

    @Query("select r from Restaurant r where r.id = :id and r.deletedAt is null and r.approved = :approved")
    Optional<Restaurant> findByIdAndDeletedAtIsNullAndApproved(long id, boolean approved);

    Optional<Restaurant> findAllByRestaurateur_IdAndDeletedAtIsNull(long restaurateurId);

    @Query("select r from Restaurant r join r.categories c where c.id = :categoryId and r.deletedAt is null")
    List<Restaurant> findAllByCategoryAndDeletedAtIsNull(long categoryId);

    @Query("select r from Restaurant r join r.categories c where c.id = :categoryId and r.deletedAt is null and r.approved = :approved")
    List<Restaurant> findAllByCategoryAndDeletedAtIsNullAndApproved(long categoryId, boolean approved);

    boolean existsByDeletedAtIsNullAndRestaurateur_Id(long restaurateurId);

    // todo implement custom query to find restaurant by name (ignore case), street, ecc (endpoint: /search)
}
