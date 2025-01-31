package com.example.foody.repository;

import com.example.foody.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Category} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link Category} entities.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}