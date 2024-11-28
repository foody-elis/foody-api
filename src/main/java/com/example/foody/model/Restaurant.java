package com.example.foody.model;

import com.example.foody.model.user.EmployeeUser;
import com.example.foody.model.user.RestaurateurUser;
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
    protected long id;

    @Column(name = "name", length = 100, nullable = false)
    protected String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    protected String description;

    @Column(name = "phone_number", length = 16, nullable = false)
    protected String phoneNumber;

    @Column(name = "seats", nullable = false)
    @Min(0)
    protected int seats;

    @Column(name = "approved", nullable = false)
    protected boolean approved;

    @ManyToMany
    @JoinTable(
            name = "restaurant_category",
            joinColumns = @JoinColumn(name = "restaurant_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false)
    )
    protected List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<Dish> dishes = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<Review> reviews = new ArrayList<>();

    // This is a OneToSeven relationship, a record for each day of the week
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<WeekDayInfo> weekDayInfos = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<Booking> bookings = new ArrayList<>();

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    protected Address address;

    @OneToOne
    @JoinColumn(name = "restaurateur_id", nullable = false)
    protected RestaurateurUser restaurateur;

    @OneToMany(mappedBy = "employerRestaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected List<EmployeeUser> employees = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(long id, String name, String description, String phoneNumber, int seats, boolean approved, List<Category> categories, List<Dish> dishes, List<Review> reviews, List<WeekDayInfo> weekDayInfos, List<Order> orders, List<Booking> bookings, Address address, RestaurateurUser restaurateur, List<EmployeeUser> employees) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.seats = seats;
        this.approved = approved;
        this.categories = categories;
        this.dishes = dishes;
        this.reviews = reviews;
        this.weekDayInfos = weekDayInfos;
        this.orders = orders;
        this.bookings = bookings;
        this.address = address;
        this.restaurateur = restaurateur;
        this.employees = employees;
    }
}
