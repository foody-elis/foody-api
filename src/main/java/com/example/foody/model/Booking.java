package com.example.foody.model;

import com.example.foody.state.booking.ActiveState;
import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.BookingStatus;
import com.example.foody.state.booking.CancelledState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bookings")
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
    @JoinColumn(name = "sitting_time_id")
    private SittingTime sittingTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Transient
    private BookingState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    public Booking() {
    }

    public Booking(long id, LocalDate date, int seats, SittingTime sittingTime, User user, Restaurant restaurant, BookingState state) {
        this.id = id;
        this.date = date;
        this.seats = seats;
        this.sittingTime = sittingTime;
        this.user = user;
        this.restaurant = restaurant;
        this.state = state;
        setStatus(state);
    }

    public BookingState getState() {
        if (state == null && status != null) {
            switch (status) {
                case BookingStatus.ACTIVE -> state = new ActiveState(this);
                case BookingStatus.CANCELLED -> state = new CancelledState(this);
            }
        }
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
        setStatus(state);
    }

    public void setStatus(BookingState state) {
        if (state != null) {
            this.status = BookingStatus.valueOf(state.getName());
        }
    }

    public void activate() {
        state.activate();
    }

    public void cancel() {
        state.cancel();
    }
}
