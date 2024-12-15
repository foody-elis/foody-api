package com.example.foody.repository;

import com.example.foody.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndDeletedAtIsNull(long id);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
}
