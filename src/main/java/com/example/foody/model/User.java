package com.example.foody.model;

import com.example.foody.utils.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
public class User extends DefaultEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", length = 320, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "surname", length = 30, nullable = false)
    private String surname;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone_number", length = 16)
    private String phoneNumber;

    @Column(name = "avatar")
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Restaurant> restaurants = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
