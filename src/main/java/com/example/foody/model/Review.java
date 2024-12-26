package com.example.foody.model;

import com.example.foody.model.user.CustomerUser;
import com.example.foody.observer.Publisher;
import com.example.foody.observer.Subscriber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reviews")
public class Review extends DefaultEntity implements Publisher<Review> {
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

    @Transient
    private List<Subscriber<Review>> subscribers = new ArrayList<>();

    public Review(long id, String title, String description, int rating, CustomerUser customer, Restaurant restaurant, Dish dish) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.customer = customer;
        this.restaurant = restaurant;
        this.dish = dish;
    }

    @Override
    public void subscribe(Subscriber<Review> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber<Review> subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.update(this));
    }
}