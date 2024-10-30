package com.example.foody.model.user;

import com.example.foody.model.*;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance
@DiscriminatorColumn(name = "role")
@Table(name = "users")
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

    @Column(name = "avatar")
    protected String avatar;

    @Column(name = "role", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected Role role;

    @Column(name = "active", nullable = false)
    protected boolean active = true;

    public User() {
    }

    public User(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.role = role;
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getRole()));
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