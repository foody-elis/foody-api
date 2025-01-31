package com.example.foody.repository;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link User} entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return an {@link Optional} containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all users by their role.
     *
     * @param role the role of the users
     * @return a list of users with the specified role
     */
    List<User> findByRole(Role role);
}