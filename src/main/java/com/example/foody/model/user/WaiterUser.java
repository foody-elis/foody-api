package com.example.foody.model.user;

import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.utils.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a waiter user in the system.
 * <p>
 * Extends the {@link EmployeeUser} class and inherits its properties and methods.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(Role.Constants.WAITER_VALUE)
public class WaiterUser extends EmployeeUser {

    /** The buyer user associated with the waiter user. */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "buyer_id", insertable = false, updatable = false))
    })
    private BuyerUser buyer;

    public WaiterUser(
            long id,
            String email,
            String password,
            String name,
            String surname,
            LocalDate birthDate,
            String phoneNumber,
            String avatarGoogleDriveFileId,
            Role role,
            boolean active,
            String firebaseCustomToken,
            Restaurant employerRestaurant,
            List<Order> orders
    ) {
        super(
                id,
                email,
                password,
                name,
                surname,
                birthDate,
                phoneNumber,
                avatarGoogleDriveFileId,
                role,
                active,
                firebaseCustomToken,
                employerRestaurant
        );
        this.buyer = new BuyerUser(id, orders);
    }

    /**
     * Gets the list of orders associated with the waiter user.
     *
     * @return the list of orders
     */
    public List<Order> getOrders() {
        return buyer.getOrders();
    }

    /**
     * Sets the list of orders associated with the waiter user.
     *
     * @param orders the list of orders to set
     */
    public void setOrders(List<Order> orders) {
        buyer.setOrders(orders);
    }

    /**
     * Deletes the waiter user and its associated orders.
     */
    @Override
    public void delete() {
        super.delete();
        this.buyer.getOrders().forEach(Order::delete);
    }
}