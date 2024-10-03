package com.example.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "restaurants")
public class Restaurant extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "phone_number", length = 16, nullable = false)
    private String phoneNumber;

    @Column(name = "seats", nullable = false)
    @Min(0)
    private int seats;

    @Column(name = "approved", nullable = false)
    private boolean approved;

    @ManyToMany
    @JoinTable(
            name = "restaurant_category",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Dish> dishes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SittingTime> sittingTimes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Restaurant() {
    }

    public Restaurant(long id, String name, String description, String phoneNumber, int seats, boolean approved, List<Category> categories, List<Dish> dishes, List<Review> reviews, List<SittingTime> sittingTimes, List<Order> orders, User user, Address address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.seats = seats;
        this.approved = approved;
        this.categories = categories;
        this.dishes = dishes;
        this.reviews = reviews;
        this.sittingTimes = sittingTimes;
        this.orders = orders;
        this.user = user;
        this.address = address;
    }
}
