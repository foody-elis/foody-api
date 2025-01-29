package com.example.foody.model;

import com.example.foody.model.user.CustomerUser;
import com.example.foody.state.booking.BookingState;
import com.example.foody.utils.enums.BookingStatus;
import com.example.foody.utils.state.BookingStateUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

/**
 * Represents a booking entity in the system.
 * <p>
 * Extends the {@link DefaultEntity} class and inherits its properties and methods.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bookings")
@SQLRestriction("deleted_at IS NULL")
public class Booking extends DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "seats", nullable = false)
    @Min(1)
    private int seats;

    @ManyToOne
    @JoinColumn(name = "sitting_time_id", nullable = false)
    private SittingTime sittingTime;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerUser customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /** The current state of the booking. */
    @Transient
    private BookingState state;

    /** The status of the booking. */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking(
            long id,
            LocalDate date,
            int seats,
            SittingTime sittingTime,
            CustomerUser customer,
            Restaurant restaurant,
            BookingState state
    ) {
        this.id = id;
        this.date = date;
        this.seats = seats;
        this.sittingTime = sittingTime;
        this.customer = customer;
        this.restaurant = restaurant;
        this.state = state;
        setStatus(state);
    }

    /**
     * Gets the current state of the booking.
     *
     * @return the current state of the booking
     */
    public BookingState getState() {
        if (state == null && status != null) {
            state = BookingStateUtils.getState(status);
        }
        return state;
    }

    /**
     * Sets the current state of the booking.
     *
     * @param state the new state of the booking
     */
    public void setState(BookingState state) {
        this.state = state;
        setStatus(state);
    }

    /**
     * Activates the booking.
     */
    public void activate() {
        state.activate(this);
    }

    /**
     * Cancels the booking.
     */
    public void cancel() {
        state.cancel(this);
    }

    /**
     * Sets the status of the booking based on its state.
     *
     * @param state the state of the booking
     */
    private void setStatus(BookingState state) {
        if (state != null) {
            this.status = state.getStatus();
        }
    }
}