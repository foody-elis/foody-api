package com.example.foody.model;

import com.example.foody.model.user.CustomerUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reviews")
public class Review extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "rating", nullable = false)
    @Min(1)
    @Max(5)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerUser customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // Nullable because the review could be about the restaurant in general
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    public Review() {
    }

    public Review(long id, String title, String description, int rating, CustomerUser customer, Restaurant restaurant, Dish dish) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.customer = customer;
        this.restaurant = restaurant;
        this.dish = dish;
    }
}