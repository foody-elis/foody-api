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

    // todo remove 'ACTIVE' string from these queries
    // colaesce() returns the first non-null argument (case: empty table so sum() returns null)
    @Query("""
            SELECT COALESCE(SUM(b.seats), 0)
            FROM Booking b
            WHERE b.deletedAt IS NULL
            AND b.status = 'ACTIVE'
            AND b.date = :date
            AND b.sittingTime.id = :sittingTimeId
            AND b.restaurant.id = :restaurantId
            """)
    long countBookedSeats(LocalDate date, long sittingTimeId, long restaurantId);

    // If the date is today, the sitting time's start must be in the future
    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.deletedAt IS NULL
            AND b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.restaurant.id = :restaurantId
            AND b.date = :date
            AND (b.date <> CURRENT_DATE OR b.sittingTime.start > CURRENT_TIME)
            """)
    boolean existsActiveFutureBookingByCustomer_IdAndRestaurant_IdAndDate(long customerId, long restaurantId, LocalDate date);

    // weekday() returns the day of the week (0 = Monday, ..., 6 = Sunday)
    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.deletedAt IS NULL
            AND b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.restaurant.id = :restaurantId
            AND b.date >= CURRENT_DATE
            AND b.sittingTime.weekDayInfo.weekDay = (CAST(WEEKDAY(CURDATE()) AS int) + 1)
            AND b.sittingTime.start <= CURTIME()
            AND b.sittingTime.end >= CURTIME()
            """)
    boolean existsActiveBookingForOrder(long customerId, long restaurantId);

    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.deletedAt IS NULL
            AND b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.restaurant.id = :restaurantId
            AND b.date <= CURRENT_DATE
            """)
    boolean existsPastActiveBookingByCustomer_IdAndRestaurant_Id(long customerId, long restaurantId);
}