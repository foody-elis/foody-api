package com.example.foody.model;

import com.example.foody.model.user.EmployeeUser;
import com.example.foody.model.user.RestaurateurUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "restaurants")
@SQLRestriction("deleted_at IS NULL")
public class Restaurant extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "name", length = 100, nullable = false)
    protected String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    protected String description;

    @Column(name = "photo_url")
    private String photoUrl;

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

    @Override
    public void delete() {
        super.delete();
        deleteChildren();
    }

    private void deleteChildren() {
        this.dishes.forEach(Dish::delete);
        this.reviews.forEach(Review::delete);
        this.weekDayInfos.forEach(WeekDayInfo::delete);
        this.orders.forEach(Order::delete);
        this.bookings.forEach(Booking::delete);
        this.employees.forEach(EmployeeUser::delete);
        Optional.ofNullable(this.address)
                .ifPresent(Address::delete);
    }
}