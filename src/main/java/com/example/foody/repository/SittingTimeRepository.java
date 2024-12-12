package com.example.foody.repository;

import com.example.foody.model.SittingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SittingTimeRepository extends JpaRepository<SittingTime, Long> {
    List<SittingTime> findAllByDeletedAtIsNull();

    Optional<SittingTime> findByIdAndDeletedAtIsNull(long id);

    List<SittingTime> findAllByDeletedAtIsNullAndWeekDayInfoRestaurantId(long restaurantId);

    List<SittingTime> findAllByDeletedAtIsNullAndWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(long restaurantId, int weekDay);

    @Query("""
            SELECT s
            FROM SittingTime s
            WHERE s.deletedAt IS NULL
            AND s.weekDayInfo.restaurant.id = :restaurantId
            AND s.weekDayInfo.weekDay = :weekDay
            AND s.start > current_timestamp
            ORDER BY s.start
            """)
    List<SittingTime> findAllByDeletedAtIsNullAndRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(long restaurantId, int weekDay);

    @Query("""
            SELECT s
            FROM SittingTime s
            WHERE s.deletedAt IS NULL
            AND s.weekDayInfo.restaurant.id = :restaurantId
            AND s.weekDayInfo.weekDay = :weekDay
            AND s.start > current_timestamp
            ORDER BY s.start
            LIMIT :limit
            """)
    List<SittingTime> findAllByDeletedAtIsNullAndRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(long restaurantId, int weekDay, int limit);
}