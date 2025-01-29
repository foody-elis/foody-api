package com.example.foody.model.user;

import com.example.foody.model.DefaultEntity;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Represents a user in the system.
 * <p>
 * Extends {@link DefaultEntity} and implements {@link UserDetails} for Spring Security integration.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance
@DiscriminatorColumn(name = "role")
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL")
public class User extends DefaultEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "email", length = 320, nullable = false, unique = true)
    protected String email;

    @Column(name = "password", length = 100, nullable = false)
    protected String password;

    @Column(name = "name", length = 30, nullable = false)
    protected String name;

    @Column(name = "surname", length = 30, nullable = false)
    protected String surname;

    @Column(name = "birth_date", nullable = false)
    protected LocalDate birthDate;

    @Column(name = "phone_number", length = 16)
    protected String phoneNumber;

    @Column(name = "avatar_url")
    protected String avatarUrl;

    @Column(name = "role", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected Role role;

    @Column(name = "active", nullable = false)
    protected boolean active = true;

    /** The Firebase custom token for the user. */
    @Column(name = "firebase_custom_token", columnDefinition = "TEXT", unique = true)
    protected String firebaseCustomToken;

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getRole()));
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return the email of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is non-expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the account is non-locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the credentials are non-expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}