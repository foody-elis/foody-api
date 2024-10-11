package com.example.foody.repository;

import com.example.foody.model.Booking;
import com.example.foody.repository.customized.CustomizedBookingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, CustomizedBookingRepository {
    List<Booking> findAllByDeletedAtIsNull();

    @Query("select b from Booking b where b.deletedAt is null and b.user.id = :userId order by b.date desc")
    List<Booking> findAllByDeletedAtIsNullAndUserOrderByDateDesc(long userId);

    @Query("select b from Booking b where b.deletedAt is null and b.restaurant.id = :restaurantId order by b.date desc")
    List<Booking> findAllByDeletedAtIsNullAndRestaurantOrderByDateDesc(long restaurantId);

    @Query("select sum(b.seats) from Booking b " +
            "where b.deletedAt is null and b.status = 'ACTIVE' " +
            "and b.date = :date and b.sittingTime.id = :sittingTimeId and b.restaurant.id = :restaurantId")
    long countBookedSeats(LocalDate date, long sittingTimeId, long restaurantId);
}
