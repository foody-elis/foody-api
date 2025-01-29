package com.example.foody.model.user;

import com.example.foody.model.Booking;
import com.example.foody.model.CreditCard;
import com.example.foody.model.Order;
import com.example.foody.model.Review;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a customer user in the system.
 * <p>
 * Extends the {@link User} class and inherits its properties and methods.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.CUSTOMER_VALUE)
public class CustomerUser extends User {

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    /**
     * The buyer user details embedded in the customer user.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "buyer_id", insertable = false, updatable = false))
    })
    private BuyerUser buyer;

    public CustomerUser(
            long id,
            String email,
            String password,
            String name,
            String surname,
            LocalDate birthDate,
            String phoneNumber,
            String avatar,
            Role role,
            boolean active,
            String firebaseCustomToken,
            CreditCard creditCard,
            List<Review> reviews,
            List<Booking> bookings,
            List<Order> orders
    ) {
        super(id, email, password, name, surname, birthDate, phoneNumber, avatar, role, active, firebaseCustomToken);
        this.creditCard = creditCard;
        this.reviews = reviews;
        this.bookings = bookings;
        this.buyer = new BuyerUser(id, orders);
    }

    /**
     * Returns the list of orders associated with the customer.
     *
     * @return the list of orders.
     */
    public List<Order> getOrders() {
        return buyer.getOrders();
    }

    /**
     * Sets the list of orders associated with the customer.
     *
     * @param orders the list of orders.
     */
    public void setOrders(List<Order> orders) {
        buyer.setOrders(orders);
    }

    /**
     * Deletes the customer user and its associated entities.
     */
    @Override
    public void delete() {
        super.delete();
        deleteChildren();
    }

    /**
     * Deletes the associated entities of the customer user.
     */
    private void deleteChildren() {
        Optional.ofNullable(this.creditCard)
                .ifPresent(CreditCard::delete);
        this.reviews.forEach(Review::delete);
        this.bookings.forEach(Booking::delete);
        this.buyer.getOrders().forEach(Order::delete);
    }
}