package com.example.foody.model;

import com.example.foody.state.booking.ActiveState;
import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.BookingStatus;
import com.example.foody.state.booking.DeletedState;
import jakarta.persistence.*;
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
    private BookingState state = new ActiveState(this);

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.ACTIVE;

    public Booking() {
    }

    public Booking(long id, LocalDate date, SittingTime sittingTime, User user, Restaurant restaurant, BookingState state) {
        this.id = id;
        this.date = date;
        this.sittingTime = sittingTime;
        this.user = user;
        this.restaurant = restaurant;
        this.state = state;
        setStatus(state);
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

    public void delete() {
        state.delete();
    }
}
