package com.example.foody.repository;

import com.example.foody.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndDeletedAtIsNull(long id);
}
