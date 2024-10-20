package com.example.foody.model.user;

import com.example.foody.model.Booking;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Review;
import com.example.foody.utils.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.CUSTOMER_VALUE)
public class CustomerUser extends User {
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public CustomerUser() {
    }

    public CustomerUser(long id, String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, String avatar, Role role, boolean active, CreditCard creditCard, List<Review> reviews, List<Booking> bookings) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, creditCard);
        this.reviews = reviews;
        this.bookings = bookings;
    }
}
