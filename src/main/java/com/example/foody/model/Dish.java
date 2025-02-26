package com.example.foody.model;

import com.example.foody.model.order_dish.OrderDish;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dish entity in the system.
 * <p>
 * Extends {@link DefaultEntity}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dishes")
@SQLRestriction("deleted_at IS NULL")
public class Dish extends DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    /**
     * The price of the dish.
     * <p>
     * Maximum value: 999,999.99 (1 million).
     */
    @Column(name = "price", precision = 8, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderDish> orderDishes = new ArrayList<>();

    /**
     * Marks the dish as deleted by setting the deletedAt timestamp to the current time.
     * <p>
     * Also marks all associated reviews as deleted.
     */
    @Override
    public void delete() {
        super.delete();
        this.reviews.forEach(Review::delete);
    }
}