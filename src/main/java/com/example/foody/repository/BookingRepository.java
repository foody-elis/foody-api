package com.example.foody.repository;

import com.example.foody.model.Booking;
import com.example.foody.repository.customized.CustomizedBookingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing {@link Booking} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link Booking} entities.
 * Also includes custom query methods for specific booking-related operations.
 */
public interface BookingRepository extends JpaRepository<Booking, Long>, CustomizedBookingRepository {

    /**
     * Finds all bookings for a specific customer, ordered by date in descending order.
     *
     * @param customerId the ID of the customer
     * @return a list of bookings for the specified customer, ordered by date in descending order
     */
    List<Booking> findAllByCustomer_IdOrderByDateDesc(long customerId);

    /**
     * Finds all bookings for a specific restaurant, ordered by date in descending order.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of bookings for the specified restaurant, ordered by date in descending order
     */
    List<Booking> findAllByRestaurant_IdOrderByDateDesc(long restaurantId);

    /**
     * Counts the number of booked seats for a specific date, sitting time, and restaurant.
     * <p>
     * coalesce() returns the first non-null argument (case: empty table so sum() returns null)
     *
     * @param date          the date of the booking
     * @param sittingTimeId the ID of the sitting time
     * @param restaurantId  the ID of the restaurant
     * @return the number of booked seats
     */
    @Query("""
            SELECT COALESCE(SUM(b.seats), 0)
            FROM Booking b
            WHERE b.status = 'ACTIVE'
            AND b.date = :date
            AND b.sittingTime.id = :sittingTimeId
            AND b.restaurant.id = :restaurantId
            """)
    long countBookedSeats(LocalDate date, long sittingTimeId, long restaurantId);

    /**
     * Checks if there is an active future booking for a specific customer, restaurant, and date.
     * <p>
     * If the date is today, the sitting time's start must be in the future
     *
     * @param customerId   the ID of the customer
     * @param restaurantId the ID of the restaurant
     * @param date         the date of the booking
     * @return true if there is an active future booking, false otherwise
     */
    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.restaurant.id = :restaurantId
            AND b.date = :date
            AND (b.date <> CURRENT_DATE OR b.sittingTime.start > CURRENT_TIME)
            """)
    boolean existsActiveFutureBookingByCustomer_IdAndRestaurant_IdAndDate(
            long customerId,
            long restaurantId,
            LocalDate date
    );

    /**
     * Checks if there is an active booking for a specific customer and restaurant.
     *
     * @param customerId   the ID of the customer
     * @param restaurantId the ID of the restaurant
     * @return true if there is an active booking, false otherwise
     */
    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.restaurant.id = :restaurantId
            AND b.date = CURRENT_DATE
            AND b.sittingTime.start <= CURTIME()
            AND b.sittingTime.end >= CURTIME()
            """)
    boolean existsCurrentActiveBooking(long customerId, long restaurantId);

    /**
     * Finds all current active bookings for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of current active bookings for the specified customer
     */
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.date = CURRENT_DATE
            AND b.sittingTime.start <= CURTIME()
            AND b.sittingTime.end >= CURTIME()
            """)
    List<Booking> findAllCurrentActiveBookingsByCustomer_Id(long customerId);

    /**
     * Checks if there is a past active booking for a specific customer and restaurant.
     *
     * @param customerId   the ID of the customer
     * @param restaurantId the ID of the restaurant
     * @return true if there is a past active booking, false otherwise
     */
    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.status = 'ACTIVE'
            AND b.customer.id = :customerId
            AND b.restaurant.id = :restaurantId
            AND b.date <= CURRENT_DATE
            """)
    boolean existsPastActiveBookingByCustomer_IdAndRestaurant_Id(long customerId, long restaurantId);
}