package com.example.foody.repository;

import com.example.foody.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Address} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link Address} entities.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}