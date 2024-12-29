package com.example.foody.repository;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByDeletedAtIsNull();
    Optional<User> findByIdAndDeletedAtIsNull(long id);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    List<User> findByRoleAndDeletedAtIsNull(Role role);
}