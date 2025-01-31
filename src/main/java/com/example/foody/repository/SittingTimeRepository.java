package com.example.foody.repository;

import com.example.foody.model.SittingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing {@link SittingTime} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link SittingTime} entities.
 */
public interface SittingTimeRepository extends JpaRepository<SittingTime, Long> {

    /**
     * Finds all sitting times by the restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of sitting times associated with the specified restaurant
     */
    List<SittingTime> findAllByWeekDayInfoRestaurantId(long restaurantId);

    /**
     * Finds all sitting times by the restaurant ID and weekday, ordered by start time.
     *
     * @param restaurantId the ID of the restaurant
     * @param weekDay the day of the week
     * @return a list of sitting times associated with the specified restaurant and weekday, ordered by start time
     */
    List<SittingTime> findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(
            long restaurantId,
            int weekDay
    );

    /**
     * Finds all sitting times by the restaurant ID and weekday, starting after the current time, ordered by start time.
     *
     * @param restaurantId the ID of the restaurant
     * @param weekDay the day of the week
     * @return a list of sitting times associated with the specified restaurant and weekday, starting after the current time, ordered by start time
     */
    @Query("""
            SELECT s
            FROM SittingTime s
            WHERE s.weekDayInfo.restaurant.id = :restaurantId
            AND s.weekDayInfo.weekDay = :weekDay
            AND s.start > current_timestamp
            ORDER BY s.start
            """)
    List<SittingTime> findAllByRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(long restaurantId, int weekDay);

    /**
     * Finds all sitting times by the restaurant ID and weekday, starting after the current time, ordered by start time, limited to a specified number of results.
     *
     * @param restaurantId the ID of the restaurant
     * @param weekDay the day of the week
     * @param limit the maximum number of results to return
     * @return a list of sitting times associated with the specified restaurant and weekday, starting after the current time, ordered by start time
     */
    @Query("""
            SELECT s
            FROM SittingTime s
            WHERE s.weekDayInfo.restaurant.id = :restaurantId
            AND s.weekDayInfo.weekDay = :weekDay
            AND s.start > current_timestamp
            ORDER BY s.start
            LIMIT :limit
            """)
    List<SittingTime> findAllByRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(
            long restaurantId,
            int weekDay,
            int limit
    );
}