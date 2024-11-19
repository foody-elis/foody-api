package com.example.foody.repository;

import com.example.foody.model.Booking;
import com.example.foody.repository.customized.CustomizedBookingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, CustomizedBookingRepository {
    List<Booking> findAllByDeletedAtIsNull();

    List<Booking> findAllByDeletedAtIsNullAndCustomer_IdOrderByDateDesc(long customerId);

    List<Booking> findAllByDeletedAtIsNullAndRestaurant_IdOrderByDateDesc(long restaurantId);

    // todo remove 'ACTIVE' string
    // colaesce() returns the first non-null argument (case: empty table so sum() returns null)
    @Query("select coalesce(sum(b.seats), 0) from Booking b " +
            "where b.deletedAt is null and b.status = 'ACTIVE' " +
            "and b.date = :date and b.sittingTime.id = :sittingTimeId and b.restaurant.id = :restaurantId")
    long countBookedSeats(LocalDate date, long sittingTimeId, long restaurantId);

    // todo remove 'ACTIVE' string
    // weekday() returns the day of the week (0 = Monday, ..., 6 = Sunday)
    @Query("select count(b) > 0 from Booking b " +
            "where b.deletedAt is null and b.status = 'ACTIVE' " +
            "and b.customer.id = :customerId and b.restaurant.id = :restaurantId " +
            "and b.date >= current_date " +
            "and b.sittingTime.weekDayInfo.weekDay = (cast(weekday(curdate()) as int) + 1) " +
            "and b.sittingTime.start <= curtime() and b.sittingTime.end >= curtime()")
    boolean existsActiveBookingForOrder(long customerId, long restaurantId);
}