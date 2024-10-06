package com.example.foody.model;

import com.example.foody.state.booking.ActiveState;
import com.example.foody.state.booking.BookingState;
import com.example.foody.state.booking.BookingStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Transient
    private BookingState state;

    public Booking() {
        this.status = BookingStatus.ACTIVE;
        this.state = new ActiveState(this);
    }

    public Booking(long id, LocalDate date, SittingTime sittingTime, User user, Restaurant restaurant) {
        this.id = id;
        this.date = date;
        this.sittingTime = sittingTime;
        this.user = user;
        this.restaurant = restaurant;
        this.status = BookingStatus.ACTIVE;
        this.state = new ActiveState(this);
    }

    public void activate() {
        state.activate();
    }

    public void delete() {
        state.delete();
    }
}
